package com.arturmaslov.tgnba.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.BaseVM
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class HomeVM(
    private val mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    private val _teamList = MutableLiveData<List<Team?>?>()
    val extTeamList: LiveData<List<Team?>?> get() = _teamList
    private val _teamSortOption = MutableLiveData<String?>()
    val extTeamSortOption: LiveData<String?> get() = _teamSortOption

    init {
        // runs every time VM is created (not view created)
        Logger.i("HomeVM created!")
    }

    fun updateLocalTeamList() {
        Logger.i("Running MainVM updateLocalTeamList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val teamRes = mainRepo.fetchTeamResponse().value
                val rowIds: MutableList<Int> = mutableListOf()
                teamRes?.data?.forEach {
                    it?.let { team -> mainRepo.insertTeam(team) }
                        ?.let { rowId -> rowIds.add(rowId.toInt()) }
                }
                Logger.d("$rowIds ids inserted into database")
                _teamList.value = mainRepo.getLocalTeams().value
                sortTeamList(TeamSortOption.NAME) // default sort
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }

    fun sortTeamList(by: TeamSortOption) {
        Logger.i("Running HomeVM sortTeamList with $by")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                _teamList.value = _teamList.value?.sortedBy {
                    when (by) {
                        TeamSortOption.NAME -> it?.name
                        TeamSortOption.CITY -> it?.city
                        TeamSortOption.CONFERENCE -> it?.conference
                    }
                }
                _teamSortOption.value = by.sortOption
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }

}