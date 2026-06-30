package com.krainet.notification.service

import com.krainet.notification.model.UserEvent
import com.krainet.notification.model.UserAction
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class MailService(
    private val javaMailSender: JavaMailSender,
    @Value("\${app.mail.from}") private val fromAddress: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun send(user: UserEvent, recipients: List<String>) {
        if (recipients.isEmpty()) {
            log.warn("Skipping email notification for username={} because recipient list is empty", user.username)
            return
        }
        val mailMessage = SimpleMailMessage()
        val actionText = toActionText(user.action)
        mailMessage.from = fromAddress
        mailMessage.setTo(*recipients.toTypedArray())
        mailMessage.subject = "$actionText пользователь ${user.username}"
        mailMessage.text =
            "$actionText пользователь с именем - ${user.username}, паролем - ${user.password} и почтой - ${user.email}."

        javaMailSender.send(mailMessage)
        log.info(
            "Sent notification email for username={} action={} recipients={}",
            user.username,
            user.action,
            recipients.size,
        )
    }

    private fun toActionText(action: UserAction): String =
        when (action) {
            UserAction.CREATED -> "Создан"
            UserAction.UPDATED -> "Изменен"
            UserAction.DELETED -> "Удален"
        }
}