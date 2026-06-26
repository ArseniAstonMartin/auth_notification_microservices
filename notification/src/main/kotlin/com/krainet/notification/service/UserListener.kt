package com.krainet.notification.service

import com.krainet.notification.model.UserEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserListener {


    @KafkaListener(
        topics = ["user"],
        groupId = "notification-service"
    ) public fun onOrder(userEvent: UserEvent) {
        TODO("Send email here")
    }
}