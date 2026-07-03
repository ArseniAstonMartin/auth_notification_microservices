package com.krainet.notification.model

data class UserEvent(
    val eventVersion: Int = 1,
    val action: UserAction,
    val username: String,
    val email: String,
    val password: String,
)

enum class UserAction {
    CREATED,
    UPDATED,
    DELETED,
}
