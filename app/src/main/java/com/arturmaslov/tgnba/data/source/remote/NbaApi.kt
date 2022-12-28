package com.arturmaslov.tgnba.data.source

import com.arturmaslov.tgnba.BuildConfig
import com.arturmaslov.tgnba.Constants
import com.arturmaslov.tgnba.data.models.ApiError
import com.arturmaslov.tgnba.data.source.remote.ApiService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.arturmaslov.tgnba.data.source.remote.Result

enum class ApiStatus { LOADING, ERROR, DONE }

object NbaApi {
    val apiService: ApiService
    private val retrofit: Retrofit

    init {
        val baseUrl = Constants.BASE_URL
        Logger.d("baseUrl is $baseUrl")

        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // use Gson
        retrofit = if (BuildConfig.DEBUG) {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okClient = OkHttpClient.Builder() // use debugging
                .addNetworkInterceptor(StethoInterceptor())
                .build()
            retrofitBuilder.client(okClient)
            retrofitBuilder.build()
        } else {
            retrofitBuilder.build()
        }
        apiService = retrofit.create(ApiService::class.java)
    }

    // checks remote response result before sending to repository
    suspend fun <T : Any> getResult(call: Call<T>): Result<T> = suspendCoroutine {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, error: Throwable) {
                Logger.e("network error: $error")
                it.resume(Result.NetworkFailure(error))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.body()?.run { it.resume(Result.Success(this)) }
                response.errorBody()?.run {
                    val apiError = Gson().fromJson(this.string(), ApiError::class.java)
                    val errorString = "${apiError.statusCode} - ${apiError.message}"
                    Logger.e("api error: $errorString")
                    it.resume(Result.ApiFailure(errorString))
                    //it.resume(Failure(HttpException(response)))
                }
            }
        })
    }
}