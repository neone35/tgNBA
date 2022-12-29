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

    val sharedResponse: LiveData<String?> get() = mainRepo.sharedResponse
    val remoteResponse: LiveData<String?> get() = mainRepo.remoteResponse

    private val _internetIsAvailable = MutableLiveData<Boolean?>()
    val extInternetAvailable: LiveData<Boolean?> get() = _internetIsAvailable
    private val _baseStatus = MutableLiveData<LoadStatus>()
    val extBaseStatus: LiveData<LoadStatus> get() = _baseStatus
    private val _baseTeam = MutableLiveData<Team?>()
    val extBaseTeam: LiveData<Team?> get() = _baseTeam

    init {
        // runs every time VM is created (not view created)
        Logger.i("baseVM created!")
        viewModelScope.launch {
            _baseStatus.value = LoadStatus.LOADING
            _internetIsAvailable.value = NetworkChecker(app).isNetworkConnected()
            _baseStatus.value = LoadStatus.DONE
        }
    }

    fun setBaseStatus(status: LoadStatus) {
        _baseStatus.value = status
    }

    fun setBaseTeam(team: Team) {
        viewModelScope.launch {
            setBaseStatus(LoadStatus.LOADING)
            try {
                _baseTeam.value = team
                setBaseStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setBaseStatus(LoadStatus.ERROR)
                Logger.e(e.message.toString())
            }
        }
    }


}