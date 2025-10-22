package com.example.fishsecure.presenter

import com.example.fishsecure.contract.NotificationContract
import com.example.fishsecure.model.NotificationModel

class NotificationPresenter(private val view: NotificationContract.View) : NotificationContract.Presenter {

    private val model = NotificationModel()

    override fun loadNotifications() {
        view.showLoading()

        // Load notifications from model
        val hasNotifications = model.hasNotifications()

        if (hasNotifications) {
            view.displayNotifications()
        } else {
            view.showError("No notifications available")
        }

        view.hideLoading()
    }

    override fun getNotificationCount(): Int {
        return model.getNotificationCount()
    }

    override fun markAllAsRead() {
        model.markAllAsRead()
        view.displayNotifications()
    }
}