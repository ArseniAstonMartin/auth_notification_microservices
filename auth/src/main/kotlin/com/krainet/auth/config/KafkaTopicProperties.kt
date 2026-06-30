package com.krainet.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.kafka")
data class KafkaTopicProperties(
    val userTopic: String,
)
