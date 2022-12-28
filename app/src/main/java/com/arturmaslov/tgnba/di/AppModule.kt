package com.arturmaslov.tgnba.di

import android.content.Context
import com.arturmaslov.tgnba.BuildConfig
import com.arturmaslov.tgnba.Constants
import com.arturmaslov.tgnba.NetworkChecker
import com.arturmaslov.tgnba.data.source.remote.ApiService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), Constants.BASE_URL) }
    single { provideApiService(get()) }
    single { provideNetworkChecker(androidContext()) }
}

private fun provideNetworkChecker(context: Context) = NetworkChecker(context)

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    baseUrl: String
): Retrofit {
    Logger.d("baseUrl is $baseUrl")

    val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
    val retrofit: Retrofit = if (BuildConfig.DEBUG) {
        retrofitBuilder.client(okHttpClient)
        retrofitBuilder.build()
    } else {
        retrofitBuilder.build()
    }
    return retrofit
}

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()