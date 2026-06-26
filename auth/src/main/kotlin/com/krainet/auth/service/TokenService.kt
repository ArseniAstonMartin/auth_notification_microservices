package com.krainet.auth.service

import com.krainet.auth.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenService(
    jwtProperties: JwtProperties
) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generateToken(
        userDetails: UserDetails,
        expirationDate: Date,
        extraClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .add(extraClaims)
            .and()
            .signWith(secretKey)
            .compact()

    fun extractEmail(token: String): String? =
        getAllClaims(token).subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)

        return email == userDetails.username && !isExpired(token)
    }

    fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser.parseSignedClaims(token).payload
    }
}