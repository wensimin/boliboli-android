package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.wensimin.boliboli_android.LoginActivity
import com.github.wensimin.boliboli_android.rest.dto.RestError
import com.github.wensimin.boliboli_android.rest.dto.base.Page
import com.github.wensimin.boliboli_android.rest.exception.AuthException
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


private const val TAG = "rest manager"

class RestManager(private val context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val converters: MutableList<HttpMessageConverter<*>> = ArrayList()
    private val globalErrorHandler: ResponseErrorHandler
    private val clientHttpRequestFactory: SimpleClientHttpRequestFactory
    private val errorCallback: Consumer<RestError>
    private val jsonMapper: ObjectMapper

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
                        preferences.edit().remove(TOKEN_KEY).apply()
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
            val url = "$RESOURCE_SERVER/$endpoint"
            val headers = this.getAuthHeader().apply {
// 非get 使用json body
                if (method != HttpMethod.GET) contentType = MediaType.APPLICATION_JSON
            }
            buildTemplate().exchange(url, method, HttpEntity(body, headers), responseType).body
        } catch (e: AuthException) {
            toLogin()
            null
        } catch (e: Exception) {
            error.accept(RestError("none", "未知错误"))
            Log.e(TAG, e.localizedMessage ?: "未知错误")
            null
        }
    }

    private fun getAuthHeader(): HttpHeaders {
        return HttpHeaders().apply {
            val authState = TokenStatus.getAuthState(preferences) ?: throw AuthException()
            val accessToken = authState.requestAccessToken(clientAuthentication, preferences)
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
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

}