package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import com.github.wensimin.boliboli_android.LoginActivity
import com.github.wensimin.boliboli_android.rest.exception.AuthException
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.util.function.Consumer

private const val TAG = "rest manager"

class RestManager(private val context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var service: AuthorizationService = AuthorizationService(context, testConfig)
    private val globalErrorHandler: ResponseErrorHandler = object : ResponseErrorHandler {
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
                else -> throw RuntimeException(response.statusText)
            }
        }
    }

    /**
     * 测试用请求
     */
    fun testRequest(
        success: Consumer<String>, error: Consumer<AuthorizationException?> = Consumer { e ->
            Log.d(TAG, "oauth2 login error" + e?.errorDescription)
        }
    ) {
        val tokenJson = preferences.getString(TOKEN_KEY, null)
        if (tokenJson == null) {
            error.accept(null)
            this.toLogin()
            return
        }
        val authState = AuthState.jsonDeserialize(tokenJson)
        authState.performActionWithFreshTokens(
            service,
            clientAuthentication
        ) { accessToken, _, ex ->
            if (ex != null) {
                error.accept(ex)
            }
            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(StringHttpMessageConverter(Charset.defaultCharset()))
            restTemplate.errorHandler = globalErrorHandler
            val headers = HttpHeaders()
            headers["Authorization"] = "Bearer $accessToken"
            val entity = HttpEntity<String>(headers)
            Thread {
                try {
                    val response = restTemplate.exchange(
                        "$RESOURCE_SERVER/public/test",
                        HttpMethod.GET,
                        entity,
                        String::class.java
                    )
                    Log.d(TAG, response.toString())
                    success.accept(response.toString())
                } catch (e: AuthException) {
                    toLogin()
                } catch (e: Exception) {
                    error.accept(null)
                    Log.e(TAG, e.localizedMessage ?: "未知错误")
                }
            }.start()

        }
    }

    private fun toLogin() {
        Log.d(TAG, "not token to login")
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

}