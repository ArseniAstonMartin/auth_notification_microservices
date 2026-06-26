package com.krainet.notification.model

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