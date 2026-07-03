package com.krainet.notification.service

import com.krainet.notification.model.UserEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserListener(
    private val mailService: MailService,
    private val adminEmailLookupService: AdminEmailLookupService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${app.kafka.user-topic}"], groupId = "\${app.kafka.group-id}")
    fun onUserEvent(userEvent: UserEvent) {
        log.info("Received user event username={} action={}", userEvent.username, userEvent.action)
        val recipients = adminEmailLookupService.getAdminEmails()
        mailService.send(userEvent, recipients)
    }
}
