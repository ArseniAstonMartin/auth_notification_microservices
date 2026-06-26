package com.krainet.auth.model

data class UserRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)
