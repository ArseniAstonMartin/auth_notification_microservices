package com.krainet.auth.service

import com.krainet.auth.model.Action
import com.krainet.auth.model.Role
import com.krainet.auth.repository.UserRepository
import com.krainet.auth.model.User
import com.krainet.auth.model.UserEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.util.Optional

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val kafkaTemplate: KafkaTemplate<String, UserEvent>
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
        if (user.role.equals(Role.USER)) {
            kafkaTemplate.send("USER", userId.toString(), user.toUserEvent(Action.UPDATE_USER))
        }

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

    private fun User.toUserEvent(action: Action): UserEvent {
        return UserEvent(
            username = this.username,
            password = this.passwordHash,
            email = this.email,
            action = action)
    }
}