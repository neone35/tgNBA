package com.arturmaslov.tgnba.di

import com.arturmaslov.tgnba.ui.home.GameVM
import com.arturmaslov.tgnba.ui.home.HomeVM
import com.arturmaslov.tgnba.ui.player.PlayerVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeVM(get(), get()) }
    viewModel { GameVM() }
    viewModel { PlayerVM() }
}