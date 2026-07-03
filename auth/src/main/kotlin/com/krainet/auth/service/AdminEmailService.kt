package com.krainet.auth.service

import com.krainet.auth.model.Role
import com.krainet.auth.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AdminEmailService(
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["adminEmails"])
    fun findAdminEmails(): List<String> {
        val emails = userRepository.findByRole(Role.ADMIN)
            .map { admin ->
                admin.email.trim()
            }
            .distinct()

        if (emails.isEmpty()) {
            log.warn("No admin recipients found for notification events")
        }

        return emails
    }
}
