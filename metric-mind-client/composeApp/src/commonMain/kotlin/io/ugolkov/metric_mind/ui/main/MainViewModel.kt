package io.ugolkov.metric_mind.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ugolkov.metric_mind.data.db.entity.TrackEntity
import io.ugolkov.metric_mind.data.db.entity.TrackRecordEntity
import io.ugolkov.metric_mind.data.repository.TrackRepository
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackCreate
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackDelete
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackRecordCreate
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackRecordDelete
import io.ugolkov.metric_mind.ui.main.MainEvent.TrackSelect
import io.ugolkov.metric_mind.util.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmName

class MainViewModel(
    private val trackRepository: TrackRepository,
) : ViewModel() {

    var trackIdSelected: Long by mutableStateOf(-1)
        private set

    var tracksUiState: List<TrackUiModel> by mutableStateOf(emptyList())
        private set

    var trackRecordsUiState: List<TrackRecordUiModel> by mutableStateOf(emptyList())
        private set

    private var trackRecordsJob: Job? = null

    init {
        trackRepository.getTracksAsFlow()
            .onEach { tracks ->
                tracksUiState = tracks.asUiList()
                when {
                    tracksUiState.isEmpty() -> trackIdSelected = -1L
                    trackIdSelected == -1L || tracksUiState.none { it.id == trackIdSelected } ->
                        onTrackSelect(TrackSelect(tracksUiState.first().id))
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        trackRecordsJob?.cancel()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is TrackSelect -> onTrackSelect(event)
            is TrackCreate -> onTrackCreate(event)
            is TrackDelete -> onTrackDelete(event)
            is TrackRecordCreate -> onTrackRecordCreate(event)
            is TrackRecordDelete -> onTrackRecordDelete(event)
        }
    }

    private fun onTrackSelect(track: TrackSelect) {
        trackIdSelected = track.id

        trackRecordsJob?.cancel()
        trackRecordsJob = trackRepository.getTrackRecordsAsFlow(trackIdSelected)
            .onEach {
                trackRecordsUiState = it.asUiList()
            }
            .launchIn(viewModelScope)
    }

    private fun onTrackCreate(track: TrackCreate) {
        viewModelScope.launch {
            val entity = TrackEntity(
                title = track.title,
                type = track.type,
                createDate = LocalDateTime.now(),
                unit = track.unit,
                visibility = track.visibility,
            )
            trackRepository.createTrack(entity)
        }
    }

    private fun onTrackDelete(track: TrackDelete) {
        viewModelScope.launch {
            trackRepository.deleteTrack(track.id)
        }
    }

    private fun onTrackRecordCreate(trackRecord: TrackRecordCreate) {
        viewModelScope.launch {
            trackRepository.createTrackRecord(
                TrackRecordEntity(
                    trackId = trackIdSelected,
                    value = trackRecord.value,
                    dateTime = trackRecord.dateTime,
                )
            )
        }
    }

    private fun onTrackRecordDelete(trackRecord: TrackRecordDelete) {
        viewModelScope.launch {
            trackRepository.deleteTrackRecord(trackRecord.id)
        }
    }

    @JvmName("asTrackUiList")
    private fun List<TrackEntity>.asUiList(): List<TrackUiModel> =
        map { TrackUiModel(it.id, it.title, it.type) }

    @JvmName("asTrackRecordUiList")
    private fun List<TrackRecordEntity>.asUiList(): List<TrackRecordUiModel> =
        map { TrackRecordUiModel(it.id, it.value, it.date.toString(), it.time.toString()) }
}