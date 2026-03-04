package io.ugolkov.metric_mind.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import io.ugolkov.metric_mind.data.db.converter.DateTimeConverter
import io.ugolkov.metric_mind.data.db.dao.TrackDao
import io.ugolkov.metric_mind.data.db.dao.TrackRecordDao
import io.ugolkov.metric_mind.data.db.entity.TrackEntity
import io.ugolkov.metric_mind.data.db.entity.TrackRecordEntity

@Database(
    entities = [
        TrackEntity::class,
        TrackRecordEntity::class,
    ],
    version = 1,
)
@TypeConverters(DateTimeConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTrackDao(): TrackDao
    abstract fun getTrackRecordDao(): TrackRecordDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}