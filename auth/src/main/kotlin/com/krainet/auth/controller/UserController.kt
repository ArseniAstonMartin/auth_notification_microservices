package com.krainet.auth.controller

import com.krainet.auth.model.Role
import com.krainet.auth.service.UserService
import com.krainet.auth.model.User
import com.krainet.auth.model.UserRequest
import com.krainet.auth.model.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired private val userService: UserService) {

    @GetMapping("")
    fun getAllUsers(): List<User> =
        userService.findAll().toList()

    @PostMapping("")
    fun createUser(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        val createdUser = userService.save(userRequest.toUser())
        return ResponseEntity(createdUser.toResponse() , HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") userId: Long): ResponseEntity<User> {
        val user = userService.findById(userId).orElse(null)
        return if (user != null) ResponseEntity(user, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/")
    fun listAll(): List<UserResponse> {
        return userService.findAll()
            .map { it.toResponse() }
            .toList()
    }

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable("id") userId: Long, @RequestBody user: User): ResponseEntity<User> {
        val updatedUser = userService.updateUserById(userId, user)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") userId: Int): ResponseEntity<User> {
        if (!userService.existsById(userId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        userService.deleteById(userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun UserRequest.toUser(): User =
            User(
                id = TODO(),
                email = this.email,
                firstName = this.firstName,
                lastName = this.lastName,
                username = this.username,
                role = Role.valueOf(this.role),
                passwordHash = TODO()
            )

    private fun User.toResponse(): UserResponse =
        UserResponse(
            id = this.id,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            username = this.username,
            role = TODO()
        )
}