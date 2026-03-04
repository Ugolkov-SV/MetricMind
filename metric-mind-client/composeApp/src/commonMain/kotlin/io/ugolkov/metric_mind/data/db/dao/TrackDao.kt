package io.ugolkov.metric_mind.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.ugolkov.metric_mind.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert
    suspend fun insert(track: TrackEntity)

    @Update
    suspend fun update(track: TrackEntity)

    @Query("SELECT * FROM track ORDER BY `order` DESC, createDate ASC")
    fun getTracksAsFlow(): Flow<List<TrackEntity>>

    @Query("DELETE FROM track WHERE id = :id")
    suspend fun delete(id: Long)
}