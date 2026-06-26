package com.krainet.auth.model

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Role
)
