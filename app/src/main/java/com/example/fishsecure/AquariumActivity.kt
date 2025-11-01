package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishsecure.contract.AquariumContract
import com.example.fishsecure.model.Aquarium
import com.example.fishsecure.presenter.AquariumPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class AquariumActivity : Activity(), AquariumContract.View {

    private lateinit var presenter: AquariumContract.Presenter
    private lateinit var adapter: AquariumAdapter

    private lateinit var btnBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    // private lateinit var progressBar: ProgressBar // Use this if you add a ProgressBar to XML

    private val adapterList = mutableListOf<Aquarium>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aquarium)

        // 1. Initialize Presenter (NO REPOSITORY)
        presenter = AquariumPresenter()
        presenter.attachView(this)

        // 2. Initialize Views (ensure these IDs match your combined XML)
        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.rv_aquariums)
        fabAdd = findViewById(R.id.fab_add_aquarium)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        // progressBar = findViewById(R.id.progressBar)

        // 3. Setup RecyclerView and Adapter
        adapter = AquariumAdapter(adapterList) { aquarium, action ->
            presenter.handleAction(aquarium, action)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 4. Setup Click Listeners
        setupClickListeners()

        // 5. Load Data
        presenter.loadAquariums()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            presenter.handleNavigation(R.id.nav_home)
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            presenter.handleNavigation(item.itemId)
            true
        }

        bottomNavigation.selectedItemId = R.id.nav_aquarium

        fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    // --- AquariumContract.View Implementations ---

    override fun showAquariums(aquariums: List<Aquarium>) {
        // This method is called whenever Firebase data changes
        adapterList.clear()
        adapterList.addAll(aquariums)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        // Example implementation
        // recyclerView.visibility = View.GONE
        // progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        // Example implementation
        // recyclerView.visibility = View.VISIBLE
        // progressBar.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }

    override fun navigateToDetail(destination: Class<*>, aquariumId: Int) {
        val intent = Intent(this, destination).apply {
            putExtra("AQUARIUM_ID", aquariumId)
        }

        try {
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            showToast("Error navigating: Check Manifest/Activity path.")
            // Log the full exception for debugging
            Log.e("NAV_ERROR", "Failed to start activity: ${e.message}", e)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showConfirmDeleteDialog(aquarium: Aquarium) {
        AlertDialog.Builder(this, R.style.CustomAlertDialogTheme)
            .setTitle("Delete Aquarium")
            .setMessage("Are you sure you want to delete ${aquarium.name}? This action cannot be undone.")
            .setPositiveButton("DELETE") { _, _ ->
                presenter.deleteAquarium(aquarium)
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    override fun showEditDialog(aquarium: Aquarium) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_aquarium, null)

        // Find the input fields using their IDs
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.edit_name)
        val descriptionInput = dialogView.findViewById<TextInputEditText>(R.id.edit_description)
        val commentInput = dialogView.findViewById<TextInputEditText>(R.id.edit_comment)

        // Find the dropdown TextView
        val statusInput = dialogView.findViewById<AutoCompleteTextView>(R.id.edit_status) // <--- Changed type

        // 1. Setup Dropdown List
        val statusOptions = listOf("Active", "Inactive")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item_dark, statusOptions)
        statusInput.setAdapter(adapter)

        // 2. Populate fields with current aquarium data
        nameInput.setText(aquarium.name)
        statusInput.setText(aquarium.status, false) // Set text, and use 'false' to prevent triggering dropdown on set
        descriptionInput.setText(aquarium.description)
        commentInput.setText(aquarium.comment)

        // 3. Build and show the AlertDialog
        AlertDialog.Builder(this, R.style.CustomAlertDialogTheme)
            .setTitle("Edit Details for ${aquarium.name}")
            .setView(dialogView)
            .setPositiveButton("SAVE") { _, _ ->
                // Capture all updated values
                val newName = nameInput.text.toString().trim()
                val newStatus = statusInput.text.toString().trim() // Value is captured from dropdown
                val newDescription = descriptionInput.text.toString().trim()
                val newComment = commentInput.text.toString().trim()

                // Basic validation for critical fields (Name, Status)
                if (newName.isNotBlank() && newStatus.isNotBlank()) {
                    val updatedAquarium = aquarium.copy(
                        name = newName,
                        status = newStatus, // New status is the selected value
                        description = newDescription,
                        comment = newComment
                    )

                    presenter.updateAquarium(updatedAquarium)
                } else {
                    showToast("Name and Status cannot be empty.")
                }
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    private fun showAddDialog() {
        // Dialog logic remains in the View as it's UI
        val nameInput = EditText(this)
        nameInput.hint = "Aquarium Name"

        AlertDialog.Builder(this, R.style.CustomAlertDialogTheme)
            .setTitle("Add New Aquarium")
            .setView(nameInput)
            .setPositiveButton("ADD") { _, _ ->
                val name = nameInput.text.toString()
                if (name.isNotBlank()) {
                    // Pass only the necessary data to the Presenter for addition
                    presenter.addNewAquarium(
                        name = name,
                        description = "Newly added.",
                        status = "Active",
                        comment = ""
                    )
                } else {
                    showToast("Name cannot be empty.")
                }
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }
}