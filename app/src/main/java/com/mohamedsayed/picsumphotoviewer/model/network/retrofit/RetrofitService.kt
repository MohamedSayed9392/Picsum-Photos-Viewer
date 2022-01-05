package com.mohamedsayed.picsumphotoviewer.model.network.retrofit

import android.util.Log
import com.mohamedsayed.picsumphotoviewer.helpers.Q
import com.mohamedsayed.picsumphotoviewer.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitService {
    val TAG = "RetrofitService"

    private var okHttpClient: OkHttpClient? = null
    private fun getOkHttpClient(): OkHttpClient {

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideUrlAndHeaderInterceptor())
            .addInterceptor(provideHeaderHttpLoggingInterceptor())
            .addInterceptor(provideBodyHttpLoggingInterceptor())
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        return okHttpClient!!
    }

    private var headerHttpLoggingInterceptor: HttpLoggingInterceptor? = null
    private fun provideHeaderHttpLoggingInterceptor(): HttpLoggingInterceptor {

        if (headerHttpLoggingInterceptor == null) {
            headerHttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i(TAG, message)
                }
            })
            headerHttpLoggingInterceptor!!.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
        }
        return headerHttpLoggingInterceptor!!
    }

    private var bodyHttpLoggingInterceptor: HttpLoggingInterceptor? = null
    private fun provideBodyHttpLoggingInterceptor(): HttpLoggingInterceptor {
        if (bodyHttpLoggingInterceptor == null) {
            bodyHttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i(TAG, message)
                }
            })
            bodyHttpLoggingInterceptor!!.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        return bodyHttpLoggingInterceptor!!
    }

    private var urlAndHeaderInterceptor: Interceptor? = null
    private fun provideUrlAndHeaderInterceptor(): Interceptor {
        if (urlAndHeaderInterceptor == null) {
            urlAndHeaderInterceptor = Interceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder()
                val url = request.url
                    .newBuilder()
                    .build()

                builder.url(url)

                chain.proceed(builder.build())
            }
        }

        return urlAndHeaderInterceptor!!
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(Q.BASE_API)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val apiService: ApiService
        get() = retrofit.create(ApiService::class.java)
}