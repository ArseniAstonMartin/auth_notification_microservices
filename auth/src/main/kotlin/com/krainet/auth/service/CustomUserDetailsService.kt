package com.krainet.auth.service

import com.krainet.auth.repository.UserRepository
import com.krainet.auth.security.ApplicationUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsernameOrEmail(username.trim())
            ?.let { ApplicationUserDetails.from(it) }
            ?: throw UsernameNotFoundException("User not found: $username")
}
