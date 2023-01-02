package com.arturmaslov.tgnba.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arturmaslov.tgnba.data.models.Game
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.data.source.MainRepository
import com.arturmaslov.tgnba.ui.BaseVM
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class GameVM(
    private val mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    private val _gameList = MutableLiveData<List<Game?>?>()
    val extGameList: LiveData<List<Game?>?> get() = _gameList
    var tempGameList: MutableList<Game?>? = mutableListOf()

    var teamId: Int? = null
    var teamName: String? = null
    var gameRVPosition: Int = 0

    var currentPage: Int = 1
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var firstVisibleItemPos: Int = 0
    var canLoadMore: Boolean = true

    init {
        Logger.i("GameVM created!")
    }

    fun fetchGameList() {
        Logger.i("Running GameVM fetchGameList")
        viewModelScope.launch {
            setLoadStatus(LoadStatus.LOADING)
            try {
                val gameRes = mainRepo.fetchGameResponse(listOf(teamId), currentPage).value
                tempGameList?.addAll(gameRes?.data?.sortedByDescending { it?.date }!!)
                _gameList.value = tempGameList
                setLoadStatus(LoadStatus.DONE)
            } catch (e: Exception) {
                setLoadStatus(LoadStatus.ERROR)
                Logger.e(e.localizedMessage!!)
            }
        }
    }
}