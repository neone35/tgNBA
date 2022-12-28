package com.arturmaslov.tgnba.data.source.local

import androidx.lifecycle.MutableLiveData
import com.arturmaslov.tgnba.data.models.Team
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDataSource(
    val mLocalDatabase: LocalDatabase,
    private val mDispatcher: CoroutineDispatcher
) : LocalData {

    private val teamDao: TeamDao? = mLocalDatabase.teamDao

    // check for local data on startup and use it before making any remote requests
    override suspend fun getLocalTeams() =
        withContext(mDispatcher) {
            Logger.i("Running getLocalTeams()")
            val liveData = MutableLiveData<Team?>()
            val localTeams = teamDao?.getTeams()
            if (localTeams != null) {
                liveData.postValue(localTeams)
                Logger.i("Success: local courier $localTeams retrieved")
            } else {
                Logger.i("Failure: unable to retrieve local courier")
            }
            liveData.apply { postValue(localTeams) }
        }


    override suspend fun deleteTeams() =
        withContext(mDispatcher) {
            Logger.i("Running deleteTeams()")
            val deletedRows = teamDao?.deleteTeams()!!
            if (deletedRows != 0) {
                Logger.i("Success: all local team data deleted")
            } else {
                Logger.i("Failure: unable to delete local team data")
            }
        }

    override suspend fun insertTeam(team: Team) =
        withContext(mDispatcher) {
            Logger.i("Running insertTeam()")
            val insertedId = teamDao?.insertTeam(team)
            if (insertedId != null) {
                Logger.i("Success: team with id ${team.id} inserted")
            } else {
                Logger.i("Failure: unable to delete local team data settings")
            }
        }

}

interface LocalData {
    suspend fun getLocalTeams(): MutableLiveData<Team?>
    suspend fun deleteTeams()
    suspend fun insertTeam(team: Team)
}