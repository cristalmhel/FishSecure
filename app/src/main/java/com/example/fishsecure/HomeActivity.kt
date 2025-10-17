package com.example.fishsecure

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.home.AquariumAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : Activity(), HomeContract.View {

    private lateinit var addNewBtn: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var adapter: AquariumAdapter
    private lateinit var greetingText: TextView
    private lateinit var dateText: TextView

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        addNewBtn = findViewById(R.id.addNewBtn)
        recyclerView = findViewById(R.id.aquariumRecyclerView)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        greetingText = findViewById(R.id.greetingText)
        dateText = findViewById(R.id.dateText)

        presenter = HomePresenter(this)

        adapter = AquariumAdapter(
            emptyList(),
            onDeleteClick = { id -> presenter.deleteAquarium(id) },
            onRenameClick = { id -> showRenameDialog(id) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addNewBtn.setOnClickListener {
            presenter.addAquarium()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { true }
                R.id.nav_parameters -> { navigateTo(ParametersActivity::class.java); true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }

        val user = UserManager.currentUser
        val firstName = user?.firstName
        greetingText.text = if (!firstName.isNullOrEmpty()) {
            "Good Day, $firstName!"
        } else {
            "Welcome!"
        }

        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())
        dateText.text = dateFormat.format(Date())
    }

    override fun showAquariums(aquariums: List<com.example.fishsecure.model.Aquarium>) {
        adapter.updateData(aquariums)
    }

    private fun showRenameDialog(aquariumId: Int) {
        val input = EditText(this)
        input.hint = "Enter new name"

        AlertDialog.Builder(this)
            .setTitle("Rename Aquarium")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    presenter.renameAquarium(aquariumId, newName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Helper
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}
