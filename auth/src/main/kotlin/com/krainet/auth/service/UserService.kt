package com.krainet.auth.service

import com.krainet.auth.repository.UserRepository
import com.krainet.auth.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.util.Optional

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) {

    fun findAll(): List<User> =
        userRepository.findAll().toList()

    fun save(user: User): User {
        val updatedUser = user.copy(passwordHash = encoder.encode(user.passwordHash).toString())
        return userRepository.save(updatedUser)
    }

    fun findById(@PathVariable("id") id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun updateUserById(userId: Long, user: User): Optional<User> {
        val userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty) {
            return userOptional
        }
        val existingUser = userOptional.get()

        val updatedUser = existingUser.copy(
            username = user.username,
            passwordHash = encoder.encode(user.passwordHash).toString(),
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            role = user.role,)
        return Optional.of<User>(userRepository.save(updatedUser))
    }
}