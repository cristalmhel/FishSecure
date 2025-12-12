package com.example.fishsecure.model

data class NotificationModel(
    val notificationId: String = "",
    val aquariumId: String = "",
    val aquariumName: String = "",
    val problem: String = "",
    val temp: Double = 0.0,
    val tds: Double = 0.0,
    val ec: Double = 0.0,
    val user: String = "",
    val timestamp: Long = 0,
    val read: Boolean = false
)