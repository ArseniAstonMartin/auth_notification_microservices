package com.krainet.notification.service

import com.krainet.notification.client.AuthAdminClient
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AdminEmailLookupService(
    private val authAdminClient: AuthAdminClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["adminEmails"], unless = "#result.isEmpty()")
    fun getAdminEmails(): List<String> {
        val response = authAdminClient.getAdminEmails()
        val emails = response.emails
            .map { it.trim() }
            .filter { it.contains('@') }
            .distinct()

        if (emails.isEmpty()) {
            log.warn("No admin emails returned from auth endpoint")
        }

        return emails
    }
}
