//package com.hogent.tictac.persistence.network
//
//import android.app.Activity
//import android.content.Context
//import android.text.TextUtils
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//
//class ServiceGenerator {
//
//    companion object {
//
//        val LOCAL_BASE_URL = "http://192.168.145.69:8080"
//        val httpClient = OkHttpClient.Builder()
//
//        val builder: Retrofit.Builder = Retrofit.Builder()
//            .baseUrl(LOCAL_BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//
//        var retrofit: Retrofit = builder.build()
//
//        fun <S> createService(serviceClass: Class<S>, activity: Activity): S {
//            val token = activity.getSharedPreferences("user", Context.MODE_PRIVATE)
//                .getString("token", null)
//            return createService(serviceClass, authToken = token)
//        }
//
//        fun <S> createService(
//            serviceClass: Class<S>,
//            authToken: String?
//        ): S {
//            if (!TextUtils.isEmpty(authToken)) {
//                val interceptor = BasicAuthInterceptor(authToken!!)
//
//                if (!httpClient.interceptors().contains(interceptor)) {
//                    httpClient.addInterceptor(interceptor)
//
//                    builder.client(httpClient.build())
//                    retrofit = builder.build()
//                }
//            }
//
//            return retrofit.create(serviceClass)
//        }
//    }
//}