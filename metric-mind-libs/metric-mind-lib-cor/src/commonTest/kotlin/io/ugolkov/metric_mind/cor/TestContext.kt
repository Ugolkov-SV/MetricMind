package io.ugolkov.metric_mind.cor

data class TestContext(
    var status: CorStatus = CorStatus.NONE,
    var some: Int = 0,
    var result: String = "",
) {
    enum class CorStatus {
        NONE,
        RUNNING,
        FAILING,
        ERROR
    }
}