package com.arturmaslov.tgnba.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arturmaslov.tgnba.NetworkChecker
import com.arturmaslov.tgnba.data.source.MainRepository

class HomeVM(
    private val mainRepo: MainRepository,
    private val networkHelper: NetworkChecker
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

}