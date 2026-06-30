package com.krainet.auth.repository

import com.krainet.auth.model.Role
import com.krainet.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    fun findByUsernameOrEmail(login: String): User?

    fun findByRole(role: Role): List<User>
}
