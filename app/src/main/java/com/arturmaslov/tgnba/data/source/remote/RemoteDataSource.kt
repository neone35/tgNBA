package com.arturmaslov.tgnba.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arturmaslov.tgnba.data.models.GameResponse
import com.arturmaslov.tgnba.data.models.PlayerResponse
import com.arturmaslov.tgnba.data.models.TeamResponse
import com.arturmaslov.tgnba.data.source.NbaApi
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call

class RemoteDataSource(
    private val nbaApi: NbaApi,
    private val mDispatcher: CoroutineDispatcher
) : RemoteData {

    // watched from main thread for toast messages
    private val _remoteResponse = MutableLiveData<String?>()
    override val remoteResponse: LiveData<String?> get() = _remoteResponse

    private suspend fun <T : Any> checkCallAndReturn(call: Call<T>, funcName: String): T? =
        withContext(mDispatcher) {
            Logger.i("Running checkCallAndReturn()")
            var resultData: T? = null
            when (val result = nbaApi.getResult(call)) {
                is Result.Success -> {
                    Logger.d("Success: remote data retrieved")
                    resultData = result.data
                }
                is Result.NetworkFailure -> _remoteResponse.postValue(result.error.toString())
                is Result.ApiFailure -> _remoteResponse.postValue(result.errorString)
                is Result.Loading -> Logger.d("$funcName is loading")
            }
            return@withContext resultData
        }

    override suspend fun fetchTeamResponse() =
        withContext(mDispatcher) {
            Logger.i("Running fetchTeamResponse()")
            val liveData = MutableLiveData<TeamResponse?>()
            val call = nbaApi.apiService.fetchTeamResponse()
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: TeamResponse? = checkCallAndReturn(call, name!!)
            liveData.postValue(resultData)
            liveData.apply { postValue(resultData) }
        }

    override suspend fun fetchGameResponse(teamIds: List<Int?>?, page: Int) =
        withContext(mDispatcher) {
            Logger.i("Running fetchGameResponse()")
            val liveData = MutableLiveData<GameResponse?>()
            val call = nbaApi.apiService.fetchGameResponse(teamIds, page)
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: GameResponse? = checkCallAndReturn(call, name!!)
            liveData.postValue(resultData)
            liveData.apply { postValue(resultData) }
        }

    override suspend fun fetchPlayerResponse(searchTerm: String) =
        withContext(mDispatcher) {
            Logger.i("Running fetchPlayerResponse()")
            val liveData = MutableLiveData<PlayerResponse?>()
            val call = nbaApi.apiService.fetchPlayerResponse(searchTerm)
            val name = object {}.javaClass.enclosingMethod?.name
            val resultData: PlayerResponse? = checkCallAndReturn(call, name!!)
            liveData.postValue(resultData)
            liveData.apply { postValue(resultData) }
        }

}

interface RemoteData {
    val remoteResponse: LiveData<String?>
    suspend fun fetchTeamResponse(): MutableLiveData<TeamResponse?>
    suspend fun fetchGameResponse(teamIds: List<Int?>?, page: Int): MutableLiveData<GameResponse?>
    suspend fun fetchPlayerResponse(searchTerm: String): MutableLiveData<PlayerResponse?>
}