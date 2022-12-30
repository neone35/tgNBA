package com.arturmaslov.tgnba.ui.player

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.BaseVM
import com.arturmaslov.tgnba.data.models.Player
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class PlayerVM(
    private val mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    private val _playerList = MutableLiveData<List<Player?>?>()
    val extPlayerList: LiveData<List<Player?>?> get() = _playerList
    private val _searchTerm = MutableLiveData<String?>()
    val extSearchTerm: LiveData<String?> get() = _searchTerm

    init {
        Logger.i("PlayerVM created!")
    }

    fun fetchPlayerList(searchTerm: String) {
        Logger.i("Running PlayerVM fetchPlayerList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val playerRes = mainRepo.fetchPlayerResponse(searchTerm).value
                _playerList.value = playerRes?.data?.sortedBy { it?.lastName }
                _searchTerm.value = searchTerm
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }
}