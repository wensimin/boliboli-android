package com.github.wensimin.boliboli_android.manager

import android.content.SharedPreferences
import net.openid.appauth.AuthState


/**
 * token object
 */
object TokenStatus {
    private var authState: AuthState? = null

    fun getAuthState(preferences: SharedPreferences): AuthState? {
        if (authState != null) {
            return authState
        }
        val tokenJson = preferences.getString(TokenManager.TOKEN_KEY, null) ?: return null
        authState = AuthState.jsonDeserialize(tokenJson)
        return authState
    }

    fun setAuthState(authState: AuthState, preferences: SharedPreferences) {
        this.authState = authState
        preferences.edit().putString(TokenManager.TOKEN_KEY, authState.jsonSerializeString()).apply()
    }

}