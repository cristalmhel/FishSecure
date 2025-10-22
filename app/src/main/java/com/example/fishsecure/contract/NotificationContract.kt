package com.example.fishsecure.contract

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