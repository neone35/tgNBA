package com.arturmaslov.tgnba.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.BaseVM
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.data.source.ApiStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class HomeVM(
    private val mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    private val _teamList = MutableLiveData<List<Team?>?>()
    val extTeamList: LiveData<List<Team?>?> get() = _teamList

    fun updateLocalTeamList() {
        Logger.i("Running MainVM updateLocalTeamList")
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                mainRepo.updateLocalTeams()
                _teamList.value = mainRepo.getLocalTeams().value
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
                Logger.e(e.localizedMessage!!)
            }
        }
    }

    fun sortTeamList(byWhat: TeamSortOption) {
        Logger.i("Running MainVM sortTeamList")
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                _teamList.value = _teamList.value?.sortedBy {
                    when (byWhat) {
                        TeamSortOption.NAME -> it?.name
                        TeamSortOption.CITY -> it?.city
                        TeamSortOption.CONFERENCE -> it?.conference
                    }
                }
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
                Logger.e(e.localizedMessage!!)
            }
        }
    }

}