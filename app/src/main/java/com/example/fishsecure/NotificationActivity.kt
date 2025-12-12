package com.example.fishsecure

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.contract.NotificationContract
import com.example.fishsecure.model.NotificationModel
import com.example.fishsecure.presenter.NotificationPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class NotificationActivity : AppCompatActivity(), NotificationContract.View {

    private lateinit var presenter: NotificationPresenter
    private lateinit var adapter: NotificationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loader: View
    private lateinit var emptyState: View
    private lateinit var btnBack: ImageView
//    private lateinit var btnMarkAll: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private var userId: String = "taling" // Replace with logged-in user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Bind views
        recyclerView = findViewById(R.id.notificationRecycler)
        loader = findViewById(R.id.loader)
        emptyState = findViewById(R.id.emptyState)
        btnBack = findViewById(R.id.btnBack)
//        btnMarkAll = findViewById(R.id.btnMarkAll)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter { id ->
            presenter.deleteNotification(id)
        }
        recyclerView.adapter = adapter

        // Presenter
        presenter = NotificationPresenter(this)
        presenter.loadNotifications(userId)

        // Swipe-to-delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val notification = adapter.getItemAt(position)

                adapter.removeItem(position)

                Snackbar.make(recyclerView, "Notification deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        adapter.restoreItem(notification, position)
                    }.show()

                presenter.deleteNotification(notification.notificationId)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Back button
        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Mark all as read
//        btnMarkAll.setOnClickListener {
//            presenter.markAllAsRead(userId)
//        }

        // BottomNavigation handling (example)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_aquarium -> {
                    startActivity(Intent(this, AquariumActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> {
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // ✅ Set the current item as selected
        bottomNavigation.selectedItemId = R.id.nav_notifications
    }

    // ----- NotificationContract.View Implementation -----
    override fun showNotifications(list: List<NotificationModel>) {
        emptyState.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.submitList(list)
    }

    override fun showEmptyState() {
        emptyState.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun showLoading(isLoading: Boolean) {
        loader.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onNotificationDeleted() {
        Toast.makeText(this, "Notification deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkedAllRead() {
        Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}


