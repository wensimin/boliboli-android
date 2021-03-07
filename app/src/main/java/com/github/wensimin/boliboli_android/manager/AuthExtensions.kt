package com.github.wensimin.boliboli_android.manager

import android.content.SharedPreferences
import com.github.wensimin.boliboli_android.rest.exception.AuthException
import net.openid.appauth.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.lang.Exception


/**
 * 进行auth的扩展方法
 * 增加同步获取刷新token的方法
 */

/**
 * 同步请求,不使用原有方法
 */
fun AuthState.requestAccessToken(clientSecretBasic: ClientSecretBasic, preferences: SharedPreferences): String {
    if (!needsTokenRefresh) {
        return this.accessToken!!
    }
    return try {
        val tokenResponse = asyncRefreshTokenRequest(createTokenRefreshRequest(), clientSecretBasic)
        this.update(tokenResponse, null)
        TokenStatus.setAuthState(this, preferences)
        tokenResponse.accessToken!!
    } catch (e: Exception) {
        // 所有错误包装授权错误返回
        throw AuthException()
    }
}

fun asyncRefreshTokenRequest(tokenRequest: TokenRequest, clientSecretBasic: ClientSecretBasic): TokenResponse {
    val restTemplate = RestTemplate(true)
    val requestHeaders = clientSecretBasic.getRequestHeaders(tokenRequest.clientId)
    val httpHeaders = HttpHeaders()
    requestHeaders.forEach { (k, v) -> httpHeaders.add(k, v) }
    httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
    val body: MultiValueMap<String, String> = LinkedMultiValueMap()
    tokenRequest.requestParameters.forEach { (k, v) -> body.add(k, v) }
    val entity = HttpEntity(body, httpHeaders)
    val response: ResponseEntity<String> = restTemplate.postForEntity(tokenRequest.configuration.tokenEndpoint.toString(), entity, String::class.java)
    return TokenResponse.Builder(tokenRequest).fromResponseJsonString(response.body).build()
}

