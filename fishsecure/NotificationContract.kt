package com.example.fishsecure

interface NotificationContract {
    interface View {
        fun displayNotifications()
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun loadNotifications()
        fun getNotificationCount(): Int
        fun markAllAsRead()
    }
}