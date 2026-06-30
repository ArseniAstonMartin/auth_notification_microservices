package com.krainet.auth.service

import com.krainet.auth.config.JwtProperties
import com.krainet.auth.security.ApplicationUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenService(
    jwtProperties: JwtProperties,
) {
    private val secretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    fun generateAccessToken(userDetails: ApplicationUserDetails, expirationMs: Long): String {
        val expirationDate = Date(System.currentTimeMillis() + expirationMs)
        return Jwts.builder()
            .subject(userDetails.username)
            .claim("userId", userDetails.userId)
            .claim("role", userDetails.role.name)
            .claim("email", userDetails.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String? =
        parseClaims(token).subject

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token) ?: return false
        return username == userDetails.username && !isExpired(token)
    }

    private fun isExpired(token: String): Boolean =
        parseClaims(token).expiration.before(Date(System.currentTimeMillis()))

    private fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
