package com.example.fishsecure.presenter

import com.example.fishsecure.contract.NotificationContract
import com.example.fishsecure.model.NotificationModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class NotificationPresenter(
    private val view: NotificationContract.View
) : NotificationContract.Presenter {
    private val dbRef = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
    .getReference("notifications")

    private var listener: ValueEventListener? = null
    private var cachedList = mutableListOf<NotificationModel>()

    override fun loadNotifications(userId: String) {
        view.showLoading(true)

        listener = dbRef.orderByChild("user")
            .equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    cachedList.clear()

                    for (item in snapshot.children) {
                        val model = item.getValue(NotificationModel::class.java)
                        if (model != null) cachedList.add(model)
                    }

                    view.showLoading(false)

                    if (cachedList.isEmpty()) {
                        view.showEmptyState()
                    } else {
                        val sorted = cachedList.sortedByDescending { it.timestamp }
                        view.showNotifications(sorted)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    view.showLoading(false)
                }
            })
    }

    override fun deleteNotification(notificationId: String) {
        dbRef.child(notificationId).removeValue()
            .addOnSuccessListener { view.onNotificationDeleted() }
    }

    override fun getNotificationCount(): Int {
        return cachedList.size
    }

    override fun markAllAsRead(userId: String) {
        val updates = HashMap<String, Any>()

        cachedList.forEach { notif ->
            updates["${notif.notificationId}/read"] = true
        }

        dbRef.updateChildren(updates)
            .addOnSuccessListener { view.onMarkedAllRead() }
    }

    override fun detach() {
        listener?.let { dbRef.removeEventListener(it) }
    }
}

