package io.ugolkov.metric_mind

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform