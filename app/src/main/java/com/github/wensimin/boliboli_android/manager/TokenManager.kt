package com.github.wensimin.boliboli_android.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.*
import net.openid.appauth.connectivity.ConnectionBuilder
import java.net.HttpURLConnection
import java.net.URL

//TODO config
const val OAUTH_SERVER: String = "http://192.168.0.201:81/authorization"

class TokenManager(
        private val context: Context,
        private val caller: ActivityResultCaller
) {
    /**
     * 认证状态
     */
    private var authState: AuthState = AuthState()

    // Client secret
    private val clientAuthentication: ClientSecretBasic = ClientSecretBasic("androidSecret")
    private val serviceConfiguration: AuthorizationServiceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse("$OAUTH_SERVER/oauth2/authorize"),  // Authorization endpoint
            Uri.parse("$OAUTH_SERVER/oauth2/token") // Token endpoint
    )
    private var authRequest: AuthorizationRequest = AuthorizationRequest.Builder(
            serviceConfiguration,
            "boliboli-android",  // Client ID
            ResponseTypeValues.CODE,
            Uri.parse("boliboli://oauth2") // Redirect URI
    ).build()
    private var service: AuthorizationService = AuthorizationService(context)

    fun login() {

        caller.registerForActivityResult(object : ActivityResultContract<AuthorizationRequest, Intent?>() {
            override fun createIntent(
                    context: Context,
                    input: AuthorizationRequest
            ): Intent {
                return service.getAuthorizationRequestIntent(authRequest)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                return intent
            }
        })
        { result ->
            if (result == null) {
                return@registerForActivityResult
            }
            val authResponse = AuthorizationResponse.fromIntent(result)
            val authException = AuthorizationException.fromIntent(result)
            authState.update(authResponse, authException)
            if (authException != null) {
                retrieveTokens(authResponse!!)
            }
        }.launch(authRequest)
    }

    private fun retrieveTokens(response: AuthorizationResponse) {
        val tokenRequest: TokenRequest = response!!.createTokenExchangeRequest()
        // test config 无视https
        val config = AppAuthConfiguration.Builder().setConnectionBuilder(TestConnectionBuilder()).build()
        val service = AuthorizationService(context, config)
        service.performTokenRequest(tokenRequest, clientAuthentication
        ) { tokenResponse, tokenException ->
            authState.update(tokenResponse, tokenException)
            // Handle token response error here
//            persistAuthState(mAuthState)
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