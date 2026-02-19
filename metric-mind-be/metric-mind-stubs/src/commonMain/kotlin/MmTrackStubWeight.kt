package ru.otus.otuskotlin.marketplace.stubs

import io.ugolkov.metric_mind.common.model.*

object MmTrackStubWeight {
    val WEIGHT_1: MmTrack
        get() = MmTrack(
            id = MmTrackId(123),
            title = "Вес",
            type = MmTrackType.FLOAT,
            createDate = 0,
            unit = "кг",
            owner = MmUserId("user-1"),
            visibility = MmVisibility.PRIVATE,
        )
}
