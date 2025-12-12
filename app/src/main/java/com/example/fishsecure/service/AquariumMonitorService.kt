package com.example.fishsecure.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.fishsecure.helper.NotificationHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Random
import java.util.UUID

class AquariumMonitorService : Service() {

    // Your custom Firebase URL + reference
    private val database = Firebase
        .database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
        .getReference("aquariums")

    private lateinit var notificationsRef: DatabaseReference

    // Aquarium info (you can change these)
    private val aquariumId = "1"
    private var aquariumName = "Koi Fish"
    private var user = "taling"

    // Safety thresholds
    private val tempMin = 18.0
    private val tempMax = 30.0
    private val tdsMin = 100.0
    private val tdsMax = 3200.0
    private val ecMin = 0.150
    private val ecMax = 6.0

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createChannel(this)

        notificationsRef = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("notifications")

        // Reference to this specific aquarium
        val singleAquariumRef = database.child(aquariumId)

        // Listen for changes
        singleAquariumRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                // Get data
                val temp = snapshot.child("temp").getValue(Double::class.java) ?: return
                val tds = snapshot.child("tds").getValue(Double::class.java) ?: return
                val ec = snapshot.child("ec").getValue(Double::class.java) ?: return
                aquariumName = snapshot.child("name").getValue(String::class.java) ?: ""
                user = snapshot.child("user").getValue(String::class.java) ?: ""

                // VALIDATION
                val tempValid = temp in tempMin..tempMax
                val tdsValid = tds in tdsMin..tdsMax
                val ecValid = ec in ecMin..ecMax

                // DETECT PROBLEM (Your provided code integrated)
                val status = when {
                    !tempValid && temp < tempMin -> "Temperature is LOW"
                    !tempValid && temp > tempMax -> "Temperature is HIGH"
                    !tdsValid && tds < tdsMin -> "TDS is LOW"
                    !tdsValid && tds > tdsMax -> "TDS is HIGH"
                    !ecValid && ec < ecMin -> "EC level is LOW"
                    !ecValid && ec > ecMax -> "EC level is HIGH"
                    tempValid && tdsValid && ecValid -> "Normal"
                    else -> "Parameters need attention"
                }

                // Only notify when there is an abnormality
                if (status != "Normal") {
                    sendNotification(status)
                    saveNotificationLog(status, temp, tds, ec)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Send notification to user's phone
    private fun sendNotification(message: String) {
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠ Aquarium Alert: $aquariumName")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Random().nextInt(9000), notification)
    }

    // Save notification to Firebase log
    private fun saveNotificationLog(problem: String, temp: Double, tds: Double, ec: Double) {
        val id = notificationsRef.push().key ?: UUID.randomUUID().toString()

        val data = mapOf(
            "notificationId" to id,
            "aquariumId" to aquariumId,
            "aquariumName" to aquariumName,
            "user" to user,
            "problem" to problem,
            "temp" to temp,
            "tds" to tds,
            "ec" to ec,
            "timestamp" to System.currentTimeMillis()
        )

        notificationsRef.child(id).setValue(data)
    }
}
