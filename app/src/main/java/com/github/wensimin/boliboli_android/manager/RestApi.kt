package com.github.wensimin.boliboli_android.manager

import android.content.Intent
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.wensimin.boliboli_android.Application
import com.github.wensimin.boliboli_android.LoginActivity
import com.github.wensimin.boliboli_android.rest.dto.RestError
import com.github.wensimin.boliboli_android.rest.dto.base.Page
import com.github.wensimin.boliboli_android.rest.exception.AuthException
import com.github.wensimin.boliboli_android.utils.logE
import com.github.wensimin.boliboli_android.utils.toastShow
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.util.function.Consumer


object RestApi {
    private val context: android.app.Application = Application.context
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val converters: MutableList<HttpMessageConverter<*>> = ArrayList()
    private val globalErrorHandler: ResponseErrorHandler
    val clientHttpRequestFactory: SimpleClientHttpRequestFactory
    private val errorCallback: Consumer<RestError>
    private val jsonMapper: ObjectMapper
    private const val RESOURCE_SERVER: String = "http://192.168.0.201:8080/boliboli-api"

    init {
        converters.add(StringHttpMessageConverter(Charset.defaultCharset()))
        converters.add(MappingJackson2HttpMessageConverter().apply {
            // 忽略多余json
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 使用kotlin模块,通过构建参数来给值
            objectMapper.registerKotlinModule()
            // 存储一个mapper引用用于转化page
            jsonMapper = objectMapper
        })
        clientHttpRequestFactory = SimpleClientHttpRequestFactory().apply {
            setReadTimeout(5000)
            setConnectTimeout(3000)
        }
        // 默认错误处理
        errorCallback = Consumer { e ->
            context.toastShow(e.message)
        }
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
                    //TODO error msg
                    else -> throw RuntimeException("未知错误")
                }
            }
        }
    }


    /**
     * 进行page请求
     */
    fun <O> getPage(endpoint: String, responseType: Class<O>, params: Map<String, Any>? = null): Page<O>? {
        val param = params?.map { (k, v) -> "$k=$v" }?.joinToString("&")
        return request("$endpoint?$param", String::class.java)?.let {
            jsonMapper.readValue(it, jsonMapper.typeFactory.constructParametricType(Page::class.java, responseType))
        }
    }

    /**
     * 进行请求
     */
    fun <O> request(
        endpoint: String, responseType: Class<O>, method: HttpMethod = HttpMethod.GET, body: Any? = null,
        error: Consumer<RestError> = errorCallback
    ): O? {
        return try {
            val url = "${RESOURCE_SERVER}/$endpoint"
            val headers = this.getAuthHeader().apply {
                // 非get 使用json body
                if (method != HttpMethod.GET) contentType = MediaType.APPLICATION_JSON
            }
            buildTemplate().exchange(url, method, HttpEntity(body, headers), responseType).body
        } catch (e: Exception) {
            errorHandler(e, error)
            return null
        }
    }

    /**
     * 错误处理,目前仅处理auth,其他错误全部按未知处理
     */
    private fun errorHandler(e: Exception, error: Consumer<RestError>) {
        if (e is AuthException) {
            toLogin()
        } else {
            //TODO 错误信息处理
            error.accept(RestError("none", "未知错误"))
            logE(e.message ?: "未知错误")
        }
    }

    private fun getAuthHeader(): HttpHeaders {
        return HttpHeaders().apply {
            val authState = TokenStatus.getAuthState(preferences) ?: throw AuthException()
            val accessToken = authState.requestAccessToken(TokenManager.clientAuthentication, preferences)
            this["Authorization"] = "Bearer $accessToken"
        }
    }

    private fun buildTemplate(): RestTemplate {
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
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

}