package com.github.wensimin.boliboli_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.openid.appauth.*


//TODO config
const val OAUTH_SERVER: String = "http://192.168.0.201:81/authorization"
const val REQUEST_CODE_AUTH: Int = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        login()
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun login() {
        // 1
        val mServiceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse("$OAUTH_SERVER/oauth2/authorize"),  // Authorization endpoint
            Uri.parse("$OAUTH_SERVER/oauth2/token") // Token endpoint
        )

        val mClientAuthentication: ClientAuthentication =
            ClientSecretBasic("secret") // Client secret
        // 2
        val authRequest = AuthorizationRequest.Builder(
            mServiceConfiguration,
            "boliboli-android",  // Client ID
            ResponseTypeValues.CODE,
            Uri.parse("boliboli://oauth2") // Redirect URI
        ).build()
        // 3
        val service = AuthorizationService(this)
        val intent = service.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(intent, REQUEST_CODE_AUTH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode != REQUEST_CODE_AUTH) {
            return
        }
        val authResponse = AuthorizationResponse.fromIntent(intent!!)
        val authException = AuthorizationException.fromIntent(intent)
        val mAuthState = AuthState(authResponse, authException)

        // Handle authorization response error here
//        retrieveTokens(authResponse)
    }

//    private fun retrieveTokens(response: AuthorizationResponse) {
//        val tokenRequest: TokenRequest = response.createTokenExchangeRequest()
//        val service = AuthorizationService(this)
//        service.performTokenRequest(
//            request, mClientAuthentication
//        ) { tokenResponse, tokenException ->
//            mAuthState.update(tokenResponse, tokenException)
//
//            // Handle token response error here
//            persistAuthState(mAuthState)
//        }
//    }
}