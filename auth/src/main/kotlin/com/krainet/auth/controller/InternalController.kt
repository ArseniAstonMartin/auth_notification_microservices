package com.krainet.auth.controller

import com.krainet.auth.config.InternalApiProperties
import com.krainet.auth.dto.AdminEmailsResponse
import com.krainet.auth.service.AdminEmailService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/internal")
class InternalController(
    private val adminEmailService: AdminEmailService,
    private val internalApiProperties: InternalApiProperties,
) {

    @GetMapping("/admin-emails")
    fun getAdminEmails(
        @RequestHeader("X-Internal-Api-Key", required = false) apiKey: String?,
    ): AdminEmailsResponse {
        if (apiKey != internalApiProperties.apiKey) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal API key")
        }
        return AdminEmailsResponse(emails = adminEmailService.findAdminEmails())
    }
}
