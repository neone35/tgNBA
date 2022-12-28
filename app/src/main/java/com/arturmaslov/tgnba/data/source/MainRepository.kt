package com.arturmaslov.tgnba.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arturmaslov.tgnba.data.models.GameResponse
import com.arturmaslov.tgnba.data.models.PlayerResponse
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.data.source.local.LocalData
import com.arturmaslov.tgnba.data.source.local.LocalDataSource
import com.arturmaslov.tgnba.data.source.remote.RemoteData
import com.arturmaslov.tgnba.data.source.remote.RemoteDataSource
import com.orhanobut.logger.Logger

class MainRepository(
    private val mLocalDataSource: LocalDataSource,
    private val mRemoteDataSource: RemoteDataSource,
    private val mSharedDataSource: SharedDataSource,
) : LocalData, RemoteData, SharedData {

    // watched from main thread for toast messages
    override val sharedResponse: LiveData<String?> get() = mSharedDataSource.sharedResponse
    override val remoteResponse: LiveData<String?> get() = mRemoteDataSource.remoteResponse

    init {
        Logger.d("Injection MainRepository")
    }

    override suspend fun getLocalTeams(): MutableLiveData<List<Team?>?> {
        return mLocalDataSource.getLocalTeams()
    }

    override suspend fun deleteTeams() {
        return mLocalDataSource.deleteTeams()
    }

    override suspend fun insertTeam(team: Team) {
        return mLocalDataSource.insertTeam(team)
    }

    override suspend fun updateLocalTeams() {
        return mRemoteDataSource.updateLocalTeams()
    }

    override suspend fun fetchGameResponse(
        teamIds: List<Int?>?,
        page: Int
    ): MutableLiveData<GameResponse?> {
        return mRemoteDataSource.fetchGameResponse(teamIds, page)
    }

    override suspend fun fetchPlayerResponse(searchTerm: String): MutableLiveData<PlayerResponse?> {
        return mRemoteDataSource.fetchPlayerResponse(searchTerm)
    }

}