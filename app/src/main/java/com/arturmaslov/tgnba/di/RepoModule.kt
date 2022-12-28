package com.arturmaslov.tgnba.di

import com.arturmaslov.tgnba.data.source.MainRepository
import com.arturmaslov.tgnba.data.source.NbaApi
import com.arturmaslov.tgnba.data.source.SharedDataSource
import com.arturmaslov.tgnba.data.source.local.LocalDataSource
import com.arturmaslov.tgnba.data.source.local.LocalDatabase
import com.arturmaslov.tgnba.data.source.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repoModule = module {
    single { provideMainRepository(get(), get(), get()) }
    single { provideLocalDataSource(get(), get()) }
    single { provideRemoteDataSource(get(), get()) }
    single { provideSharedDataSource(get(), get(), get()) }
    single { provideDispatcherIO() }
}

private fun provideDispatcherIO() = Dispatchers.IO

private fun provideMainRepository(
    dispatcher: CoroutineDispatcher,
    nbaApi: NbaApi,
    localDB: LocalDatabase
): MainRepository {
    val localDataSource = provideLocalDataSource(localDB, dispatcher)
    val remoteDataSource = provideRemoteDataSource(nbaApi, dispatcher)
    val sharedDataSource = provideSharedDataSource(localDataSource, remoteDataSource, dispatcher)
    return MainRepository(localDataSource, remoteDataSource, sharedDataSource)
}

private fun provideLocalDataSource(
    localDB: LocalDatabase,
    dispatcher: CoroutineDispatcher
): LocalDataSource {
    return LocalDataSource(localDB, dispatcher)
}

private fun provideRemoteDataSource(
    nbaApi: NbaApi,
    dispatcher: CoroutineDispatcher
): RemoteDataSource {
    return RemoteDataSource(nbaApi, dispatcher)
}

private fun provideSharedDataSource(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource,
    dispatcher: CoroutineDispatcher
)
        : SharedDataSource {
    return SharedDataSource(localDataSource, remoteDataSource, dispatcher)
}