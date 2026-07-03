package com.krainet.auth.controller

import com.krainet.auth.dto.AdminEmailsResponse
import com.krainet.auth.service.AdminEmailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AdminEmailsController(
    private val adminEmailService: AdminEmailService,
) {

    @GetMapping("/admin-emails")
    fun getAdminEmails(): AdminEmailsResponse =
        AdminEmailsResponse(emails = adminEmailService.findAdminEmails())
}
