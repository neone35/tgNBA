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

    init {
        // runs every time VM is created (not view created)
        Logger.i("HomeVM created!")
    }

    fun updateLocalTeamList() {
        Logger.i("Running MainVM updateLocalTeamList")
        viewModelScope.launch {
            setBaseStatus(LoadStatus.LOADING)
            try {
                mainRepo.updateLocalTeamList()
                _teamList.value = mainRepo.getLocalTeams().value
                setBaseStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setBaseStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }

    fun sortTeamList(byWhat: TeamSortOption) {
        Logger.i("Running MainVM sortTeamList")
        viewModelScope.launch {
            setBaseStatus(LoadStatus.LOADING)
            try {
                _teamList.value = _teamList.value?.sortedBy {
                    when (byWhat) {
                        TeamSortOption.NAME -> it?.name
                        TeamSortOption.CITY -> it?.city
                        TeamSortOption.CONFERENCE -> it?.conference
                    }
                }
                setBaseStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setBaseStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }

}