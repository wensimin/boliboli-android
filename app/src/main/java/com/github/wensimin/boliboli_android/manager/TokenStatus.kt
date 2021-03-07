package com.github.wensimin.boliboli_android.manager

import android.content.SharedPreferences
import net.openid.appauth.AuthState

const val TOKEN_KEY = "TOKEN_KEY"

/**
 * token object
 */
object TokenStatus {
    private var authState: AuthState? = null

    fun getAuthState(preferences: SharedPreferences): AuthState? {
        if (authState != null) {
            return authState
        }
        val tokenJson = preferences.getString(TOKEN_KEY, null) ?: return null
        authState = AuthState.jsonDeserialize(tokenJson)
        return authState
    }

    fun setAuthState(authState: AuthState, preferences: SharedPreferences) {
        this.authState = authState
        preferences.edit().putString(TOKEN_KEY, authState.jsonSerializeString()).apply()
    }

}