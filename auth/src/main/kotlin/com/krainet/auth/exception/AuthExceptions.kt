package com.krainet.auth.exception

class UserNotFoundException(message: String) : RuntimeException(message)

class DuplicateUserException(message: String) : RuntimeException(message)