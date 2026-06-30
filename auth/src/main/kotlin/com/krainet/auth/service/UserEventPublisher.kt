package com.krainet.auth.service

import com.krainet.auth.config.KafkaTopicProperties
import com.krainet.auth.model.UserAction
import com.krainet.auth.model.UserEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class UserEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, UserEvent>,
    private val kafkaTopicProperties: KafkaTopicProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun publishUserAction(
        action: UserAction,
        username: String,
        email: String,
        plainPassword: String,
    ) {
        val event = UserEvent(
            action = action,
            username = username,
            email = email,
            password = plainPassword,
        )

        try {
            kafkaTemplate.send(kafkaTopicProperties.userTopic, username, event)
            log.info("Published user event action={} username={}", action, username)
        } catch (ex: Exception) {
            log.error("Failed to publish user event action={} username={}: {}", action, username, ex.message)
            throw ex
        }
    }
}
