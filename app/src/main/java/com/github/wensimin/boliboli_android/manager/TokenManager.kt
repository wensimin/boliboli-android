package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.preference.PreferenceManager
import com.github.wensimin.boliboli_android.LoginActivity
import net.openid.appauth.*
import net.openid.appauth.connectivity.ConnectionBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Consumer


//TODO config
const val OAUTH_SERVER: String = "http://192.168.0.201:81/authorization"
const val RESOURCE_SERVER: String = "http://192.168.0.201:8080/boliboli-api"

/**
 * 认证状态
 */
private const val TAG: String = "TOKEN MANAGER"


// Client secret
val clientAuthentication: ClientSecretBasic = ClientSecretBasic("androidSecret")

// test config 无视https
val testConfig = AppAuthConfiguration.Builder().setConnectionBuilder(TokenManager.TestConnectionBuilder()).build()

class TokenManager(
    context: LoginActivity,
    private var success: Runnable = Runnable {},
    private var error: Consumer<AuthorizationException?> = Consumer { e ->
        Log.d(TAG, "oauth2 login error " + e?.errorDescription)
    }
) {
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

    private var service: AuthorizationService = AuthorizationService(context, testConfig)
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val authState: AuthState = AuthState()
    private val launcher = context.registerForActivityResult(object :
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
    }

    /**
     * 进行oauth2 login
     */
    fun login(success: Runnable, error: Consumer<AuthorizationException?> = this.error) {
        this.success = success
        this.error = error
        launcher.launch(authRequest)
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
        service.performTokenRequest(tokenRequest, clientAuthentication) { tokenResponse, tokenException ->
            //save token
            authState.update(tokenResponse, tokenException)
            if (tokenException != null) {
                error.accept(tokenException)
            } else {
                //save token
                TokenStatus.setAuthState(authState, preferences)
                success.run()
            }
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