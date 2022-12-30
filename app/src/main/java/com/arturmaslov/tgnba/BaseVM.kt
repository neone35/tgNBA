package com.arturmaslov.tgnba

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.arturmaslov.tgnba.utils.NetworkChecker
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

open class BaseVM(
    private val mainRepo: MainRepository,
    private val app: Application
) : ViewModel() {

    val remoteResponse: LiveData<String?> get() = mainRepo.remoteResponse

    private val _internetIsAvailable = MutableLiveData<Boolean?>()
    val extInternetAvailable: LiveData<Boolean?> get() = _internetIsAvailable
    private val _loadStatus = MutableLiveData<LoadStatus>()
    val extLoadStatus: LiveData<LoadStatus> get() = _loadStatus
    private val _baseTeam = MutableLiveData<Team?>()
    val extBaseTeam: LiveData<Team?> get() = _baseTeam

    init {
        // runs every time VM is created (not view created)
        viewModelScope.launch {
            _loadStatus.value = LoadStatus.LOADING
            _internetIsAvailable.value = NetworkChecker(app).isNetworkConnected()
            _loadStatus.value = LoadStatus.DONE
        }
    }

    fun setLoadStatus(status: LoadStatus) {
        Logger.i("Running BaseVM setBaseStatus with $status")
        _loadStatus.value = status
    }

    fun setBaseTeam(team: Team) {
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                _baseTeam.value = team
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.message.toString())
            }
        }
    }


}