package com.example.flickrproject.network.base

import android.util.Log
import com.example.flickrproject.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class BaseNetworkManager :
    IBaseNetworkManager {

    private var retrofit: Retrofit? = null


    override fun buildRetrofit(): Retrofit {
        if (retrofit == null) {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val request: Request =
                        chain
                            .request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("charset", "UTF-8")
                            .build()
                    return@addInterceptor chain.proceed(request)
                }.build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        }

        return retrofit as Retrofit

    }
}