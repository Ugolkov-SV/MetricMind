package io.ugolkov.metric_mind.kafka

import io.ugolkov.metric_mind.biz.helper.controllerHelper
import io.ugolkov.metric_mind.common.TrackContext
import io.ugolkov.metric_mind.common.TrackFilterContext
import io.ugolkov.metric_mind.common.TrackRecordContext
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackFilterV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackRecordV1
import io.ugolkov.metric_mind.kafka.strategy.ConsumerStrategyTrackV1
import io.ugolkov.metric_mind.kafka.strategy.IConsumerStrategy
import io.ugolkov.metric_mind.logger.base.ILogWrapper
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class KafkaApp(
    private val config: KafkaConfig,
    consumerStrategies: List<IConsumerStrategy<*>>,
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) : AutoCloseable {

    private val process: AtomicBoolean = atomic(true)

    private val topicsAndStrategyByInputTopic: Map<String, TopicsAndStrategy> =
        consumerStrategies.associate { strategy ->
            val topics = strategy.topics(config)
            topics.input to TopicsAndStrategy(topics.input, topics.output, strategy)
        }

    private val log: ILogWrapper = config.corSettings.loggerProvider.logger(this::class)

    fun start() =
        runBlocking { run() }

    override fun close() {
        process.value = false
    }

    private suspend fun run() {
        process.value = true

        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val records = Dispatchers.IO {
                    consumer.poll(Duration.ofSeconds(1))
                }

                if (!records.isEmpty) {
                    log.debug("Receive ${records.count()} messages")
                }

                records.forEach { record ->
                    runCatching {
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: error("Topic ${record.topic()} not found")

                        val response = strategy.controllerHelper(
                            record = record,
                            clazz = KafkaConsumer::class,
                            logId = "kafka-consumer",
                        )
                        sendResponse(response, outputTopic)
                    }
                        .onFailure { error ->
                            log.error("error", e = error)
                        }
                }
            }
        } catch (e: WakeupException) {

        } catch (e: RuntimeException) {
            withContext(NonCancellable) {
                throw e
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private suspend fun IConsumerStrategy<*>.controllerHelper(
        record: ConsumerRecord<String, String>,
        clazz: KClass<*>,
        logId: String,
    ): String =
        when (val strategy = this) {
            is ConsumerStrategyTrackV1 -> config.controllerHelper<TrackContext, String>(
                getRequest = { strategy.deserialize(record.value(), this) },
                toResponse = { strategy.serialize(this) },
                clazz = clazz,
                logId = logId,
            )

            is ConsumerStrategyTrackRecordV1 -> config.controllerHelper<TrackRecordContext, String>(
                getRequest = { strategy.deserialize(record.value(), this) },
                toResponse = { strategy.serialize(this) },
                clazz = clazz,
                logId = logId,
            )

            is ConsumerStrategyTrackFilterV1 -> config.controllerHelper<TrackFilterContext, String>(
                getRequest = { strategy.deserialize(record.value(), this) },
                toResponse = { strategy.serialize(this) },
                clazz = clazz,
                logId = logId,
            )

            else -> error("Unknown strategy ${strategy.javaClass.simpleName}")
        }

    @OptIn(ExperimentalUuidApi::class)
    private fun sendResponse(response: String, outputTopic: String) {
        val responseRecord = ProducerRecord(
            outputTopic,
            Uuid.random().toString(),
            response,
        )
        log.info("sending ${responseRecord.key()} to $outputTopic:\n$response")
        producer.send(responseRecord)
    }

    private data class TopicsAndStrategy(
        val inputTopic: String,
        val outputTopic: String,
        val strategy: IConsumerStrategy<*>,
    )
}