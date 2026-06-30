package com.krainet.auth.controller

import com.krainet.auth.dto.AuthenticationResponse
import com.krainet.auth.dto.LoginRequest
import com.krainet.auth.dto.RegistrationResponse
import com.krainet.auth.dto.RegisterUserRequest
import com.krainet.auth.dto.UserResponse
import com.krainet.auth.service.AuthenticationService
import com.krainet.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthenticationResponse =
        authenticationService.login(request)

    @PostMapping("/register/user")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@Valid @RequestBody request: RegisterUserRequest): RegistrationResponse {
        val createdUser = userService.registerUser(request)
        val authResponse = authenticationService.login(
            LoginRequest(login = request.username, password = request.password),
        )
        return RegistrationResponse(
            user = createdUser,
            accessToken = authResponse.accessToken,
            tokenType = authResponse.tokenType,
        )
    }

    @PostMapping("/register/admin")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerAdmin(@Valid @RequestBody request: RegisterUserRequest): RegistrationResponse {
        val createdUser = userService.registerAdmin(request)
        val authResponse = authenticationService.login(
            LoginRequest(login = request.username, password = request.password),
        )
        return RegistrationResponse(
            user = createdUser,
            accessToken = authResponse.accessToken,
            tokenType = authResponse.tokenType,
        )
    }
}
