package com.hogent.tictac.persistence.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicAuthInterceptor(token: String) : Interceptor {

    private val authToken: String = token

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", authToken).build()
        return chain.proceed(authenticatedRequest)
    }
}