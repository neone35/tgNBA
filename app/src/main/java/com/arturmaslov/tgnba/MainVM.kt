package com.arturmaslov.tgnba

import android.app.Application
import com.arturmaslov.tgnba.data.source.MainRepository

class MainVM(
    mainRepo: MainRepository,
    app: Application
) : BaseVM(mainRepo, app) {

}