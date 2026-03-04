package io.ugolkov.metric_mind.di

import androidx.room.Room
import androidx.room.RoomDatabase
import io.ugolkov.metric_mind.data.db.AppDatabase
import org.koin.core.scope.Scope
import java.io.File

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File("build/tmp", "app.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}