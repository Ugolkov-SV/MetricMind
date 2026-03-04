package io.ugolkov.metric_mind.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import io.ugolkov.metric_mind.data.db.AppDatabase
import org.koin.core.scope.Scope

actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = get<Context>()
    val dbFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}