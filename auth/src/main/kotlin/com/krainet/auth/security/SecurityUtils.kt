package com.krainet.auth.security

import com.krainet.auth.model.Role
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {

    fun getCurrentUserDetails(): ApplicationUserDetails =
        SecurityContextHolder.getContext().authentication?.principal as? ApplicationUserDetails
            ?: throw IllegalStateException("Authenticated user is not available")

    fun getCurrentUserId(): Long = getCurrentUserDetails().userId

    fun getCurrentUserRole(): Role = getCurrentUserDetails().role

    fun isAdmin(): Boolean = getCurrentUserRole() == Role.ADMIN
}
