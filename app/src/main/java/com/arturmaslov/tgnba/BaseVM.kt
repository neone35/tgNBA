package com.arturmaslov.tgnba

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.data.source.ApiStatus
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

    private val internetIsAvailable = MutableLiveData<Boolean?>()
    val extInternetAvailable: LiveData<Boolean?> get() = internetIsAvailable
    val status = MutableLiveData<ApiStatus>()
    val extStatus: LiveData<ApiStatus> get() = status

    init {
        // runs every time VM is created (not view created)
        Logger.i("baseVM created!")
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            internetIsAvailable.value = NetworkChecker(app).isNetworkConnected()
            status.value = ApiStatus.DONE
        }
    }

}