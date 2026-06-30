package com.krainet.auth.security

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component("userAuthorizationService")
class UserAuthorizationService(
    private val securityUtils: SecurityUtils,
) {

    fun canAccessUser(userId: Long, authentication: Authentication): Boolean {
        val principal = authentication.principal as? ApplicationUserDetails ?: return false
        return principal.role.name == "ADMIN" || principal.userId == userId
    }

    fun isAdmin(authentication: Authentication): Boolean {
        val principal = authentication.principal as? ApplicationUserDetails ?: return false
        return principal.role.name == "ADMIN"
    }

    fun isCurrentUser(userId: Long): Boolean =
        securityUtils.getCurrentUserId() == userId
}
