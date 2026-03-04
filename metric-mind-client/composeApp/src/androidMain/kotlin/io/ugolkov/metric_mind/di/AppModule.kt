package io.ugolkov.metric_mind.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication

fun KoinApplication.config(context: Context) {
    androidLogger()
    androidContext(context)
}