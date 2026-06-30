package com.krainet.auth.mapper

import com.krainet.auth.dto.RegisterUserRequest
import com.krainet.auth.dto.UpdateUserRequest
import com.krainet.auth.dto.UserResponse
import com.krainet.auth.model.Role
import com.krainet.auth.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toEntity(request: RegisterUserRequest, encodedPassword: String, role: Role): User =
        User(
            username = request.username.trim(),
            passwordHash = encodedPassword,
            email = request.email.trim().lowercase(),
            firstName = request.firstName.trim(),
            lastName = request.lastName.trim(),
            role = role,
        )

    fun applyUpdate(existing: User, request: UpdateUserRequest, encodedPassword: String?): User =
        User(
            id = existing.id,
            username = request.username?.trim() ?: existing.username,
            passwordHash = encodedPassword ?: existing.passwordHash,
            email = request.email?.trim()?.lowercase() ?: existing.email,
            firstName = request.firstName?.trim() ?: existing.firstName,
            lastName = request.lastName?.trim() ?: existing.lastName,
            role = request.role ?: existing.role,
        )

    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            role = user.role,
        )
}
