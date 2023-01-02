package com.arturmaslov.tgnba.ui.main

import android.app.Application
import com.arturmaslov.tgnba.data.source.MainRepository
import com.arturmaslov.tgnba.ui.BaseVM
import com.orhanobut.logger.Logger

class MainVM(
    mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

    init {
        // runs every time VM is created (not view created)
        Logger.i("MainVM created!")
    }

}