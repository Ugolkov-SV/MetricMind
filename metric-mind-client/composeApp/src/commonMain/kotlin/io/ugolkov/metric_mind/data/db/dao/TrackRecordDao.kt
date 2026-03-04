package io.ugolkov.metric_mind.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.ugolkov.metric_mind.data.db.entity.TrackRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackRecordDao {
    @Insert
    suspend fun insert(trackRecord: TrackRecordEntity)

    @Update
    suspend fun update(trackRecord: TrackRecordEntity)

    @Query("SELECT * FROM track_record WHERE trackId = :trackId ORDER BY dateTime DESC")
    fun getTrackRecordsAsFlow(trackId: Long): Flow<List<TrackRecordEntity>>

    @Query("DELETE FROM track_record WHERE id = :id")
    suspend fun delete(id: Long)
}