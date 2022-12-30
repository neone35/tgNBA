package com.arturmaslov.tgnba.di

import com.arturmaslov.tgnba.BaseVM
import com.arturmaslov.tgnba.ui.home.GameVM
import com.arturmaslov.tgnba.ui.home.HomeVM
import com.arturmaslov.tgnba.ui.main.MainVM
import com.arturmaslov.tgnba.ui.player.PlayerVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeVM(get(), androidApplication()) }
    viewModel { GameVM() }
    viewModel { PlayerVM(get(), androidApplication()) }
    viewModel { MainVM(get(), androidApplication()) }
    viewModel { BaseVM(get(), androidApplication()) }
}