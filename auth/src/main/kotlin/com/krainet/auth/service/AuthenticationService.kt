package com.krainet.auth.service

import com.krainet.auth.dto.AuthenticationResponse
import com.krainet.auth.dto.LoginRequest
import com.krainet.auth.security.ApplicationUserDetails
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    @Value($$"${jwt.access-token-expiration}") private val accessTokenExpiration: Long,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun login(request: LoginRequest): AuthenticationResponse {
        log.info("Login attempt for login={}", request.login)

        authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.login.trim(), request.password),
        )

        val userDetails = userDetailsService.loadUserByUsername(request.login.trim()) as ApplicationUserDetails
        val accessToken = tokenService.generateAccessToken(
            userDetails,
            accessTokenExpiration,
        )

        log.info("Login successful for userId={} username={}", userDetails.userId, userDetails.username)
        return AuthenticationResponse(accessToken = accessToken)
    }
}
