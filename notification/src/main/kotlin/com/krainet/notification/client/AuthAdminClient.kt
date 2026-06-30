package com.krainet.notification.client

import com.krainet.notification.dto.AdminEmailsResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "auth-admin-client",
    url = "\${clients.auth.base-url}",
)
interface AuthAdminClient {

    @GetMapping("/api/internal/admin-emails")
    fun getAdminEmails(
        @RequestHeader("X-Internal-Api-Key") apiKey: String,
    ): AdminEmailsResponse
}
