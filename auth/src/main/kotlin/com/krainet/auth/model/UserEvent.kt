package com.krainet.auth.model

data class UserEvent (
    val username: String,
    val email: String,
    val password: String,
    val action: Action
)

enum class Action() {
    DELETE_USER,
    UPDATE_USER,
    CREATE_USER
}