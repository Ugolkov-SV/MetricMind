package io.ugolkov.metric_mind

import android.app.Application
import io.ugolkov.metric_mind.di.config
import io.ugolkov.metric_mind.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            config(this@App)
        }
    }
}