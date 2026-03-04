package io.ugolkov.metric_mind.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ugolkov.metric_mind.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

val databaseModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().getTrackDao() }
    single { get<AppDatabase>().getTrackRecordDao() }
}