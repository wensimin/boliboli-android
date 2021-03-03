package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.*
import net.openid.appauth.connectivity.ConnectionBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.function.Consumer


//TODO config
const val OAUTH_SERVER: String = "http://192.168.0.201:81/authorization"
const val RESOURCE_SERVER: String = "http://192.168.0.201:8080/boliboli-api"

/**
 * 认证状态
 */
private val authState: AuthState = AuthState()
private const val TAG: String = "TOKEN MANAGER"

class TokenManager(
    private val context: Context,
    private val caller: ActivityResultCaller
) {


    // Client secret
    private val clientAuthentication: ClientSecretBasic = ClientSecretBasic("androidSecret")
    private val serviceConfiguration: AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse("$OAUTH_SERVER/oauth2/authorize"),  // Authorization endpoint
            Uri.parse("$OAUTH_SERVER/oauth2/token") // Token endpoint
        )
    private var authRequest: AuthorizationRequest = AuthorizationRequest.Builder(
        serviceConfiguration,
        "boliboli-android",  // Client ID
        ResponseTypeValues.CODE,
        Uri.parse("boliboli://oauth2") // Redirect URI
    ).setScope("profile openid") //scope
        .build()
    private var service: AuthorizationService = AuthorizationService(context)


    /**
     * 进行oauth2 login
     */
    fun login(
        success: Runnable, error: Consumer<AuthorizationException?> = Consumer { e ->
            Log.d(TAG, "oauth2 login error" + e?.errorDescription)
        }
    ) {
        caller.registerForActivityResult(object :
            ActivityResultContract<AuthorizationRequest, Intent?>() {
            override fun createIntent(context: Context, input: AuthorizationRequest): Intent {
                return service.getAuthorizationRequestIntent(authRequest)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                return intent
            }
        }) { result ->
            if (result == null) {
                error.accept(null)
                return@registerForActivityResult
            }
            val authResponse = AuthorizationResponse.fromIntent(result)
            val authException = AuthorizationException.fromIntent(result)
            authState.update(authResponse, authException)
            if (authResponse != null) {
                retrieveTokens(authResponse, success, error)
            } else {
                error.accept(authException)
            }
        }.launch(authRequest)
    }

    /**
     * 使用code请求token并且save至authState
     */
    private fun retrieveTokens(
        response: AuthorizationResponse,
        success: Runnable,
        error: Consumer<AuthorizationException?>
    ) {
        val tokenRequest: TokenRequest = response.createTokenExchangeRequest()
        // test config 无视https
        val config =
            AppAuthConfiguration.Builder().setConnectionBuilder(TestConnectionBuilder()).build()
        val service = AuthorizationService(context, config)
        service.performTokenRequest(
            tokenRequest, clientAuthentication
        ) { tokenResponse, tokenException ->
            //save token
            authState.update(tokenResponse, tokenException)
            if (tokenException != null) {
                error.accept(tokenException)
            } else {
                success.run()
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
        authState.performActionWithFreshTokens(
            service,
            clientAuthentication
        ) { accessToken, _, ex ->
            if (ex != null) {
                error.accept(ex)
            }
            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(StringHttpMessageConverter(Charset.defaultCharset()))
            val headers = HttpHeaders()
            headers["Authorization"] = "Bearer $accessToken"
            val entity = HttpEntity<String>(headers)
            Thread {
                val response = restTemplate.exchange(
                    "$RESOURCE_SERVER/public/test",
                    HttpMethod.GET,
                    entity,
                    String::class.java
                )
                Log.d(TAG, response.toString())
                success.accept(response.toString())
            }.start()

        }
    }

    /**
     * 测试用conn 无视https
     */
    class TestConnectionBuilder : ConnectionBuilder {
        override fun openConnection(uri: Uri): HttpURLConnection {
            val conn = URL(uri.toString()).openConnection() as HttpURLConnection
            conn.connectTimeout = 1500
            conn.readTimeout = 2000
            conn.instanceFollowRedirects = false
            return conn
        }
    }

}