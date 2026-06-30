package com.krainet.auth.security

import com.krainet.auth.model.Role
import com.krainet.auth.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class ApplicationUserDetails(
    val userId: Long,
    private val login: String,
    private val passwordHash: String,
    val email: String,
    val role: Role,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun from(user: User): ApplicationUserDetails =
            ApplicationUserDetails(
                userId = user.id,
                login = user.username,
                passwordHash = user.passwordHash,
                email = user.email,
                role = user.role,
            )
    }
}
