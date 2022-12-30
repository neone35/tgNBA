package com.arturmaslov.tgnba.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.BaseVM
import com.arturmaslov.tgnba.data.models.Game
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class GameVM(
    private val mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    private val _gameList = MutableLiveData<List<Game?>?>()
    val extGameList: LiveData<List<Game?>?> get() = _gameList

    init {
        Logger.i("GameVM created!")
    }

    fun fetchGameList(teamIds: List<Int?>, page: Int) {
        Logger.i("Running GameVM fetchGameList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val gameRes = mainRepo.fetchGameResponse(teamIds, page).value
                _gameList.value = gameRes?.data?.sortedByDescending { it?.date }
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }
}