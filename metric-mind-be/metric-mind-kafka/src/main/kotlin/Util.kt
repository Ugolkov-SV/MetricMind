package io.ugolkov.metric_mind.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

fun KafkaConfig.createKafkaConsumer(): KafkaConsumer<String, String> {
    val properties = Properties(4)
        .apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
            put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        }
    return KafkaConsumer<String, String>(properties)
}

fun KafkaConfig.createKafkaProducer(): KafkaProducer<String, String> {
    val properties = Properties(3)
        .apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        }
    return KafkaProducer<String, String>(properties)
}