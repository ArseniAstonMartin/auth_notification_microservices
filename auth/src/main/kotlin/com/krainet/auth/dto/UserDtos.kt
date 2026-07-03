package com.krainet.auth.dto

import com.krainet.auth.model.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val password: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "First name is required")
    @field:Size(max = 100, message = "First name must not exceed 100 characters")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 100, message = "Last name must not exceed 100 characters")
    val lastName: String,
)

data class UpdateUserRequest(
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username: String? = null,

    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val password: String? = null,

    @field:Email(message = "Email must be valid")
    val email: String? = null,

    @field:Size(max = 100, message = "First name must not exceed 100 characters")
    val firstName: String? = null,

    @field:Size(max = 100, message = "Last name must not exceed 100 characters")
    val lastName: String? = null,
)

data class LoginRequest(
    @field:NotBlank(message = "Login is required")
    val login: String,

    @field:NotBlank(message = "Password is required")
    val password: String,
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Role,
)

data class AuthenticationResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
)

data class RegistrationResponse(
    val user: UserResponse,
    val accessToken: String,
    val tokenType: String = "Bearer",
)

data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String>? = null,
)
