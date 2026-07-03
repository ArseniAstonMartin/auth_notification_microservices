package com.krainet.notification.service

import com.krainet.notification.client.AdminEmailsClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AdminEmailLookupService(
    private val adminEmailsClient: AdminEmailsClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getAdminEmails(): List<String> {
        val response = adminEmailsClient.getAdminEmails()
        val emails = response.emails
            .map { it.trim() }
            .distinct()

        if (emails.isEmpty()) {
            log.warn("No admin emails returned from auth endpoint")
        }

        return emails
    }
}
