package com.krainet.notification.client

import com.krainet.notification.dto.AdminEmailsResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "auth-admin-client",
    url = "\${clients.auth.base-url}",
)
interface AuthAdminClient {

    @GetMapping("/api/admin-emails")
    fun getAdminEmails(): AdminEmailsResponse
}
