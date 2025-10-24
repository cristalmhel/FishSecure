package com.example.fishsecure

class NotificationModel {

    // Sample notification data matching your static layout
    private val notifications = listOf(
        NotificationData("High temperature detected!", "Consider replacing the water.", false),
        NotificationData("Low pH detected!", "Consider monitoring the water.", false),
        NotificationData("Dissolved oxygen is too low!", "Check the tank conditions.", false)
    )

    data class NotificationData(
        val title: String,
        val description: String,
        var isRead: Boolean
    )

    fun hasNotifications(): Boolean {
        return notifications.isNotEmpty()
    }

    fun getNotificationCount(): Int {
        return notifications.size
    }

    fun getUnreadCount(): Int {
        return notifications.count { !it.isRead }
    }

    fun markAllAsRead() {
        notifications.forEach { it.isRead = true }
    }

    fun getAllNotifications(): List<NotificationData> {
        return notifications
    }
}