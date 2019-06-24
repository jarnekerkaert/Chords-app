package com.hogent.tictac.persistence.network

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    val LOCAL_BASE_URL = "http://192.168.145.69:8080"

    @Provides
    internal fun provideSongApi(retrofit: Retrofit): SongApiService {
        return retrofit.create(SongApiService::class.java)
    }

    @Provides
    internal fun provideUserApi(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    internal fun provideRetrofitInterface(context: Context, authToken: String): Retrofit {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        Log.i("AUTH_TOKEN", authToken)
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            if (authToken != "none")
                addInterceptor(BasicAuthInterceptor(authToken))
            else
                addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(LOCAL_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    internal fun getAuthToken(context: Context): String {
        return context
            .getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("token", "none")!!
    }
}