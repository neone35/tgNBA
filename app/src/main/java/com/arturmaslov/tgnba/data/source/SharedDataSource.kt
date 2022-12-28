package com.arturmaslov.tgnba.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arturmaslov.tgnba.data.models.TeamResponse
import com.arturmaslov.tgnba.data.source.local.LocalDataSource
import com.arturmaslov.tgnba.data.source.remote.RemoteDataSource
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import com.arturmaslov.tgnba.data.source.remote.Result

// methods using local and remote sources
class SharedDataSource(
    mLocalDataSource: LocalDataSource,
    mRemoteDataSource: RemoteDataSource,
    private val mDispatcher: CoroutineDispatcher
) : SharedData {

    private val mTeamDao = mLocalDataSource.mLocalDatabase.teamDao
    private val mNbaApi = mRemoteDataSource.mNbaApi

    // watched from main thread for toast messages
    private val _sharedResponse = MutableLiveData<String?>()
    override val sharedResponse: LiveData<String?> get() = _sharedResponse

    init {
        mRemoteDataSource.mSharedDataSource = this
    }

    private suspend fun <T> saveOnRemoteSuccess(result: Result.Success<T>) {
        Logger.i("Running saveOnRemoteSuccess()")
        withContext(mDispatcher) {
            val rowIds: MutableList<Int> = mutableListOf()
            when (result.data) {
                is TeamResponse -> {
                    result.data.data?.forEach {
                        rowIds.add(mTeamDao?.insertTeam(it)!!.toInt())
                    }
                    Logger.d("Inserting teams ${result.data}")
                }
                else -> Logger.d(rowIds)
            }
            Logger.d("$rowIds ids inserted into database")
        }
    }

    suspend fun <T : Any> checkCallResultAndSave(call: Call<T>, funcName: String) {
        Logger.i("Running checkCallResultAndSave()")
        withContext(mDispatcher) {
            when (val result = mNbaApi.getResult(call)) {
                is Result.Success -> {
                    Logger.d("Success: remote data retrieved")
                    saveOnRemoteSuccess(result)
                }
                is Result.NetworkFailure -> _sharedResponse.postValue(result.error.toString())
                is Result.ApiFailure -> _sharedResponse.postValue(result.errorString)
                is Result.Loading -> Logger.d("$funcName is loading")
            }
        }
    }
}

interface SharedData {
    val sharedResponse: LiveData<String?>
}