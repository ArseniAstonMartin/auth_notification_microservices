package com.krainet.notification.service

import com.krainet.notification.client.AuthAdminClient
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AdminEmailLookupService(
    private val adminEmailClient: AdminEmailClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["adminEmails"], unless = "#result.isEmpty()")
    fun getAdminEmails(): List<String> {
        val response = adminEmailClient.getAdminEmails()
        val emails = response.emails
            .map { it.trim() }
            .distinct()

        if (emails.isEmpty()) {
            log.warn("No admin emails returned from auth endpoint")
        }

        return emails
    }
}
