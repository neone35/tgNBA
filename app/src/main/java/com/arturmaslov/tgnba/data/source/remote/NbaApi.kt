package com.arturmaslov.tgnba.data.source

import com.arturmaslov.tgnba.data.models.ApiError
import com.arturmaslov.tgnba.data.source.remote.ApiService
import com.arturmaslov.tgnba.data.source.remote.Result
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class ApiStatus { LOADING, ERROR, DONE }

class NbaApi(val apiService: ApiService) {

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