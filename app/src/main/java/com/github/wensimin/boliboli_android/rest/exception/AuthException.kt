package com.github.wensimin.boliboli_android.rest.exception

import java.lang.RuntimeException

class AuthException : RuntimeException()

class SystemException(val type: String, override val message: String) : RuntimeException()