package com.krainet.auth.service

import com.krainet.auth.dto.RegisterUserRequest
import com.krainet.auth.dto.UpdateUserRequest
import com.krainet.auth.dto.UserResponse
import com.krainet.auth.exception.DuplicateUserException
import com.krainet.auth.exception.UserNotFoundException
import com.krainet.auth.mapper.UserMapper
import com.krainet.auth.model.Role
import com.krainet.auth.model.User
import com.krainet.auth.model.UserAction
import com.krainet.auth.repository.UserRepository
import com.krainet.auth.security.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Caching
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val userEventPublisher: UserEventPublisher,
    private val securityUtils: SecurityUtils,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    @CacheEvict(cacheNames = ["adminEmails"], allEntries = true)
    fun registerUser(request: RegisterUserRequest): UserResponse =
        registerByRole(request, Role.USER)

    @Transactional
    @CacheEvict(cacheNames = ["adminEmails"], allEntries = true)
    fun registerAdmin(request: RegisterUserRequest): UserResponse =
        registerByRole(request, Role.ADMIN)

    private fun registerByRole(request: RegisterUserRequest, role: Role): UserResponse {
        validateUniqueCredentials(request.username, request.email)
        val encodedPassword: String = passwordEncoder.encode(request.password) ?: request.password
        val saved = userRepository.save(userMapper.toEntity(request, encodedPassword, role))
        log.info("Registered user id={} username={} role={}", saved.id, saved.username, saved.role)

        if (role == Role.USER) {
            userEventPublisher.publishUserAction(
                action = UserAction.CREATED,
                username = saved.username,
                email = saved.email,
                plainPassword = request.password,
            )
        }

        return userMapper.toResponse(saved)
    }

    @Cacheable(cacheNames = ["users"], key = "#id")
    @Transactional(readOnly = true)
    fun getById(id: Long): UserResponse {
        val user = findUserOrThrow(id)
        return userMapper.toResponse(user)
    }

    @Transactional(readOnly = true)
    fun getCurrentUser(): UserResponse =
        getById(securityUtils.getCurrentUserId())

    @Transactional(readOnly = true)
    fun getAll(): List<UserResponse> =
        userRepository.findAll().map { userMapper.toResponse(it) }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(cacheNames = ["users"], key = "#id"),
            CacheEvict(cacheNames = ["adminEmails"], allEntries = true),
        ],
    )
    fun update(id: Long, request: UpdateUserRequest): UserResponse {
        val existing = findUserOrThrow(id)
        validateUpdateConflicts(existing, request)

        val plainPassword = request.password
        val encodedPassword = plainPassword?.let { passwordEncoder.encode(it) }
        val updated = userRepository.save(userMapper.applyUpdate(existing, request, encodedPassword))
        log.info("Updated user id={} username={}", updated.id, updated.username)

        if (shouldPublishUserEvent()) {
            userEventPublisher.publishUserAction(
                action = UserAction.UPDATED,
                username = updated.username,
                email = updated.email,
                plainPassword = plainPassword ?: "unchanged",
            )
        }

        return userMapper.toResponse(updated)
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(cacheNames = ["users"], key = "#id"),
            CacheEvict(cacheNames = ["adminEmails"], allEntries = true),
        ],
    )
    fun delete(id: Long) {
        val existing = findUserOrThrow(id)
        val publishEvent = shouldPublishUserEvent()

        userRepository.delete(existing)
        log.info("Deleted user id={} username={}", existing.id, existing.username)

        if (publishEvent) {
            userEventPublisher.publishUserAction(
                action = UserAction.DELETED,
                username = existing.username,
                email = existing.email,
                plainPassword = "removed",
            )
        }
    }

    private fun shouldPublishUserEvent(): Boolean =
        !securityUtils.isAdmin()

    private fun findUserOrThrow(id: Long): User =
        userRepository.findById(id).orElseThrow {
            UserNotFoundException("User not found: $id")
        }

    private fun validateUniqueCredentials(username: String, email: String) {
        if (userRepository.existsByUsername(username.trim())) {
            throw DuplicateUserException("Username already taken: ${username.trim()}")
        }
        if (userRepository.existsByEmail(email.trim().lowercase())) {
            throw DuplicateUserException("Email already taken: ${email.trim().lowercase()}")
        }
    }

    private fun validateUpdateConflicts(existing: User, request: UpdateUserRequest) {
        request.username?.trim()?.takeIf { it != existing.username }?.let { username ->
            if (userRepository.existsByUsername(username)) {
                throw DuplicateUserException("Username already taken: $username")
            }
        }

        request.email?.trim()?.lowercase()?.takeIf { it != existing.email }?.let { email ->
            if (userRepository.existsByEmail(email)) {
                throw DuplicateUserException("Email already taken: $email")
            }
        }
    }
}
