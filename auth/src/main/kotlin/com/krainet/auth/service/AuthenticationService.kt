package com.krainet.auth.service

import com.krainet.auth.config.JwtProperties
import com.krainet.auth.dto.AuthenticationResponse
import com.krainet.auth.dto.LoginRequest
import com.krainet.auth.security.ApplicationUserDetails
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
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
            jwtProperties.accessTokenExpiration,
        )

        log.info("Login successful for userId={} username={}", userDetails.userId, userDetails.username)
        return AuthenticationResponse(accessToken = accessToken)
    }
}
