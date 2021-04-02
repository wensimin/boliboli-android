package com.github.wensimin.boliboli_android.manager

import androidx.preference.PreferenceManager
import com.github.wensimin.boliboli_android.Application
import net.openid.appauth.AuthState


/**
 * token object
 */
object TokenStatus {
    private var authState: AuthState? = null
    private val preferences = PreferenceManager.getDefaultSharedPreferences(Application.context)

    fun getAuthState(): AuthState? {
        if (authState != null) {
            return authState
        }
        val tokenJson = preferences.getString(TokenManager.TOKEN_KEY, null) ?: return null
        authState = AuthState.jsonDeserialize(tokenJson)
        return authState
    }

    fun setAuthState(authState: AuthState) {
        this.authState = authState
        preferences.edit().putString(TokenManager.TOKEN_KEY, authState.jsonSerializeString()).apply()
    }

}