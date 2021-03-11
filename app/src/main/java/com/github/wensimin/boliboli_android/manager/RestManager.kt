package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.wensimin.boliboli_android.LoginActivity
import com.github.wensimin.boliboli_android.rest.dto.RestError
import com.github.wensimin.boliboli_android.rest.exception.AuthException
import com.github.wensimin.boliboli_android.utils.toastShow
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.time.Duration
import java.util.function.Consumer


private const val TAG = "rest manager"

class RestManager(private val context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val messageConverters: MutableList<HttpMessageConverter<*>> = ArrayList()
    private val globalErrorHandler: ResponseErrorHandler
    private val clientHttpRequestFactory: SimpleClientHttpRequestFactory

    init {
        messageConverters.add(StringHttpMessageConverter(Charset.defaultCharset()))
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
        val objectMapper = mappingJackson2HttpMessageConverter.objectMapper
        // 忽略多余json
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 使用kotlin模块,通过构建参数来给值
        objectMapper.registerKotlinModule()
        messageConverters.add(mappingJackson2HttpMessageConverter)
        clientHttpRequestFactory = SimpleClientHttpRequestFactory()
        clientHttpRequestFactory.setReadTimeout(5000)
        clientHttpRequestFactory.setConnectTimeout(3000)
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
     * nobody 版本
     *  RestManager@request
     */
    fun <O> request(endpoint: String, responseType: Class<O>): O? {
        return this.request(endpoint = endpoint, responseType = responseType, body = null)
    }

    /**
     * 进行请求
     */
    fun <I, O> request(
        endpoint: String, method: HttpMethod = HttpMethod.GET, body: I? = null, responseType: Class<O>,
        error: Consumer<RestError> = Consumer { e ->
            context.toastShow("请求错误 ${e.message}")
        }
    ): O? {
        return try {
            val authState = TokenStatus.getAuthState(preferences) ?: throw AuthException()
            val accessToken = authState.requestAccessToken(clientAuthentication, preferences)
            val url = "$RESOURCE_SERVER/$endpoint"
            val restTemplate = this.getTemplate()
            val headers = HttpHeaders()
            headers["Authorization"] = "Bearer $accessToken"
            val entity = HttpEntity<I>(body, headers)
            restTemplate.exchange(url, method, entity, responseType).body
        } catch (e: AuthException) {
            toLogin()
            null
        } catch (e: Exception) {
            error.accept(RestError("none", "未知错误"))
            Log.e(TAG, e.localizedMessage ?: "未知错误")
            null
        }
    }

    private fun getTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.messageConverters = messageConverters
        restTemplate.errorHandler = globalErrorHandler
        restTemplate.requestFactory = clientHttpRequestFactory
        return restTemplate
    }

    /**
     * 去往登录activity
     */
    private fun toLogin() {
        context.toastShow("未登录,请进行登录")
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

}