package com.krainet.auth.controller

import com.krainet.auth.dto.UpdateUserRequest
import com.krainet.auth.dto.UserResponse
import com.krainet.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/me")
    fun getCurrentUser(): UserResponse =
        userService.getCurrentUser()

    @GetMapping
    @PreAuthorize("@userAuthorizationService.isAdmin(authentication)")
    fun getAllUsers(): List<UserResponse> =
        userService.getAll()

    @GetMapping("/{id}")
    @PreAuthorize("@userAuthorizationService.canAccessUser(#id, authentication)")
    fun getUserById(@PathVariable id: Long): UserResponse =
        userService.getById(id)

    @PutMapping("/{id}")
    @PreAuthorize("@userAuthorizationService.canAccessUser(#id, authentication)")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest,
    ): UserResponse =
        userService.update(id, request)

    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizationService.canAccessUser(#id, authentication)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        userService.delete(id)
    }
}
