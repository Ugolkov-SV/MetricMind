package io.ugolkov.metric_mind.di

import io.ugolkov.metric_mind.data.repository.TrackRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    includes(databaseModule)
    singleOf(::TrackRepository)
}