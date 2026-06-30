package com.krainet.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("clients.auth")
data class InternalClientProperties(
    val baseUrl: String,
    val apiKey: String,
)
