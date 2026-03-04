package io.ugolkov.metric_mind.di

import io.ugolkov.metric_mind.ui.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    includes(repositoryModule)
    viewModelOf(::MainViewModel)
}