package com.example.fishsecure.contract

import com.example.fishsecure.model.NotificationModel

interface NotificationContract {

    interface View {
        fun showNotifications(list: List<NotificationModel>)
        fun showEmptyState()
        fun showLoading(isLoading: Boolean)
        fun onNotificationDeleted()
        fun onMarkedAllRead()
    }

    interface Presenter {
        fun loadNotifications(userId: String)
        fun deleteNotification(notificationId: String)
        fun getNotificationCount(): Int
        fun markAllAsRead(userId: String)
        fun detach()
    }
}
