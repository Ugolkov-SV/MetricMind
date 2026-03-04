package io.ugolkov.metric_mind.data.repository

import io.ugolkov.metric_mind.data.db.dao.TrackDao
import io.ugolkov.metric_mind.data.db.dao.TrackRecordDao
import io.ugolkov.metric_mind.data.db.entity.TrackEntity
import io.ugolkov.metric_mind.data.db.entity.TrackRecordEntity
import kotlinx.coroutines.flow.Flow

class TrackRepository(
    private val trackDao: TrackDao,
    private val trackRecordDao: TrackRecordDao,
) {
    fun getTracksAsFlow(): Flow<List<TrackEntity>> =
        trackDao.getTracksAsFlow()

    suspend fun createTrack(track: TrackEntity) =
        trackDao.insert(track)

    suspend fun deleteTrack(id: Long) =
        trackDao.delete(id)

    fun getTrackRecordsAsFlow(trackId: Long): Flow<List<TrackRecordEntity>> =
        trackRecordDao.getTrackRecordsAsFlow(trackId)

    suspend fun createTrackRecord(trackRecord: TrackRecordEntity) =
        trackRecordDao.insert(trackRecord)

    suspend fun deleteTrackRecord(id: Long) =
        trackRecordDao.delete(id)
}