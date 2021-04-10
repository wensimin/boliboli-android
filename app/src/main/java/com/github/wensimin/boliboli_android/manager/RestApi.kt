package com.github.wensimin.boliboli_android.manager

import android.content.Intent
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.wensimin.boliboli_android.Application
import com.github.wensimin.boliboli_android.LoginActivity
import com.github.wensimin.boliboli_android.rest.exception.AuthException
import com.github.wensimin.boliboli_android.rest.exception.SystemException
import com.github.wensimin.boliboli_android.rest.pojo.ErrorType
import com.github.wensimin.boliboli_android.rest.pojo.RestError
import com.github.wensimin.boliboli_android.rest.pojo.RestResponse
import com.github.wensimin.boliboli_android.rest.pojo.base.Page
import com.github.wensimin.boliboli_android.utils.logE
import com.github.wensimin.boliboli_android.utils.toastShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.io.InputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object RestApi {
    private val context: android.app.Application = Application.context
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val converters: MutableList<HttpMessageConverter<*>> = ArrayList()
    private val globalErrorHandler: ResponseErrorHandler
    private val clientHttpRequestFactory: SimpleClientHttpRequestFactory
    private val jsonMapper: ObjectMapper
    const val RESOURCE_SERVER: String = "https://boliboli.xyz:3000/boliboli-api"

    init {
        converters.apply {
            add(StringHttpMessageConverter(Charset.defaultCharset()))
            add(FormHttpMessageConverter())
            add(MappingJackson2HttpMessageConverter().apply {
                //时间格式
                objectMapper.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).apply {
                    timeZone = TimeZone.getTimeZone("GMT+8")
                }
                // 忽略多余json
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 使用kotlin模块,通过构建参数来给值
                objectMapper.registerKotlinModule()
                // 存储一个mapper引用用于转化page
                jsonMapper = objectMapper
            })
        }
        clientHttpRequestFactory = SimpleClientHttpRequestFactory().apply {
            setReadTimeout(5000)
            setConnectTimeout(3000)
        }
        // 错误监听
        globalErrorHandler = object : ResponseErrorHandler {
            override fun hasError(response: ClientHttpResponse): Boolean {
                return response.statusCode != HttpStatus.OK
            }

            override fun handleError(response: ClientHttpResponse) {
                when (response.statusCode) {
                    HttpStatus.UNAUTHORIZED -> {
                        preferences.edit().remove(TokenManager.TOKEN_KEY).apply()
                        throw AuthException()
                    }
                    else -> {
                        throwError(response.body)
                    }
                }
            }
        }
    }

    /**
     * 从body 解析json error
     */
    private fun throwError(body: InputStream?) {
        val restError = jsonMapper.readValue(body, RestError::class.java)
        //处理刷新token过期
        throw if (restError.error == "invalid_grant") AuthException() else SystemException(
            restError.error ?: ErrorType.ERROR.name,
            restError.message ?: "未知错误"
        )
    }


    /**
     * 进行page请求
     * page 请求目前必然为get
     */
    fun <O> getPageAsync(
        endpoint: String,
        responseType: Class<O>,
        params: Map<String, Any>? = null
    ): RestResponse<Page<O>> {
        val param = params?.map { (k, v) -> "$k=$v" }?.joinToString("&")
        return requestAsync("$endpoint?$param", String::class.java).let {
            if (it.data != null) {
                val page: Page<O> = jsonMapper.readValue(
                    it.data,
                    jsonMapper.typeFactory.constructParametricType(Page::class.java, responseType)
                )
                RestResponse(page)
            } else {
                RestResponse(error = it.error)
            }
        }
    }

    /**
     * get page 协程版本
     */
    suspend fun <O> getPage(
        endpoint: String,
        responseType: Class<O>,
        params: Map<String, Any>? = null
    ): RestResponse<Page<O>> {
        return withContext(Dispatchers.IO) {
            getPageAsync(endpoint, responseType, params)
        }
    }

    /**
     * 请求协程版本
     */
    suspend fun <O> request(
        endpoint: String, responseType: Class<O>, method: HttpMethod = HttpMethod.GET, body: Any? = null
    ): RestResponse<O> {
        return withContext(Dispatchers.IO) {
            requestAsync(endpoint, responseType, method, body)
        }
    }

    /**
     * 进行请求
     */
    fun <O> requestAsync(
        endpoint: String, responseType: Class<O>, method: HttpMethod = HttpMethod.GET, body: Any? = null
    ): RestResponse<O> {
        return try {
            val url = "${RESOURCE_SERVER}/$endpoint"
            val headers = this.getAuthHeader().apply {
                // 非get 使用json body
                if (method != HttpMethod.GET) contentType = MediaType.APPLICATION_JSON
            }
            RestResponse(data = buildTemplate().exchange(url, method, HttpEntity(body, headers), responseType).body)
        } catch (e: Exception) {
            //处理一轮错误
            errorHandler(e)
            RestResponse(error = e)
        }
    }

    /**
     * 错误处理,目前仅处理auth,其他错误全部按未知处理
     */
    private fun errorHandler(e: Exception) {
        if (e is AuthException) {
            toLogin()
        } else {
            //TODO 错误信息处理
            logE(e.message ?: "未知错误")
        }
    }

    private fun getAuthHeader(): HttpHeaders {
        return HttpHeaders().apply {
            val authState = TokenStatus.getAuthState() ?: throw AuthException()
            val accessToken = authState.requestAccessToken(TokenManager.clientAuthentication)
            this["Authorization"] = "Bearer $accessToken"
        }
    }

    /**
     * 异步方法,在io进程获取header
     * 需要自己处理AuthException
     */
    suspend fun getAuthHeaderInIo(): HttpHeaders {
        return withContext(Dispatchers.IO) { getAuthHeader() }
    }

    fun buildTemplate(): RestTemplate {
        return RestTemplate().apply {
            messageConverters = converters
            errorHandler = globalErrorHandler
            requestFactory = clientHttpRequestFactory
        }
    }

    /**
     * 去往登录activity
     */
    private fun toLogin() {
        context.toastShow("未登录,请进行登录")
        context.startActivity(Intent(context, LoginActivity::class.java).apply {
            //TODO 这个flag似乎导致两个应用在场,需要验证
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }


}