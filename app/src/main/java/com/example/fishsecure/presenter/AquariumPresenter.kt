package com.example.fishsecure.presenter

import com.example.fishsecure.AquariumAdapter
import com.example.fishsecure.HomeActivity
import com.example.fishsecure.NotificationActivity
import com.example.fishsecure.ParametersActivity
import com.example.fishsecure.R
import com.example.fishsecure.SettingsActivity
import com.example.fishsecure.UserManager
import com.example.fishsecure.contract.AquariumContract
import com.example.fishsecure.model.Aquarium
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AquariumPresenter : AquariumContract.Presenter {

    private var view: AquariumContract.View? = null
    // Use the exact database reference from your code
    private val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("aquariums")
    private val valueEventListener = createAquariumValueEventListener()

    // --- MVP Lifecycle ---

    override fun attachView(view: AquariumContract.View) {
        this.view = view
        // Start listening for data immediately
        database.addValueEventListener(valueEventListener)
    }

    override fun detachView() {
        // Stop listening for data to prevent memory leaks
        database.removeEventListener(valueEventListener)
        this.view = null
    }

    // --- Data Listener (Firebase Realtime) ---

    // Inside AquariumPresenter.kt

    private fun createAquariumValueEventListener(): ValueEventListener {
        // Get the current user's username once
        val currentUsername = UserManager.currentUser?.username.toString()

        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val aquariums = mutableListOf<Aquarium>()

                for (aquariumSnapshot in snapshot.children) {
                    // 1. Attempt to parse the Firebase key as an integer ID
                    val keyId = aquariumSnapshot.key?.toIntOrNull()

                    // 2. Get the Aquarium object
                    val aquarium = aquariumSnapshot.getValue(Aquarium::class.java)

                    // 3. Validate and filter the data
                    if (keyId != null && keyId > 0 && aquarium != null) {
                        // Check if the aquarium belongs to the current user
                        if (aquarium.user == currentUsername) {
                            // Set the ID using the validated Firebase key and add to the list
                            aquariums.add(aquarium.copy(id = keyId))
                        }
                    }
                }

                // Sort and push data to the View
                val sortedList = aquariums.sortedBy { it.id }
                view?.showAquariums(sortedList)
                view?.hideLoading()

                if (aquariums.isEmpty()) {
                    view?.showErrorMessage("No aquariums found for $currentUsername. Add one!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                view?.hideLoading()
                view?.showErrorMessage("Failed to load aquariums: ${error.message}")
            }
        }
    }

    // --- Data Operations (CRUD) ---

    override fun loadAquariums() {
        view?.showLoading()
        // Data is loaded automatically when listener is attached
    }

    override fun addNewAquarium(name: String, description: String, status: String, comment: String) {
        // Find the next available ID
        database.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Get the last key and ensure it's a valid integer, otherwise start at 0
                val lastId = snapshot.children.firstOrNull()?.key?.toIntOrNull() ?: 0
                val newId = (lastId + 1).toString()
                val username = UserManager.currentUser?.username.toString()

                val newAquarium = Aquarium(
                    id = newId.toInt(),
                    name = name,
                    description = description,
                    status = status,
                    comment = comment,
                    user = username
                )

                // Save to Firebase using the ID as the key
                database.child(newId).setValue(newAquarium)
                view?.showToast("Aquarium added successfully.")

                // NOTE: The card will appear automatically because the above setValue()
                // triggers the createAquariumValueEventListener, which refreshes the list in the View.
            }

            override fun onCancelled(error: DatabaseError) {
                view?.showErrorMessage("Failed to add aquarium: ${error.message}")
            }
        })
    }

    override fun updateAquarium(aquarium: Aquarium) {
        database.child(aquarium.id.toString()).setValue(aquarium)
        view?.showToast("${aquarium.name} updated successfully.")
    }

    override fun deleteAquarium(aquarium: Aquarium) {
        database.child(aquarium.id.toString()).removeValue()
        view?.showToast("${aquarium.name} deleted.")
    }

    // --- UI/Action Handlers ---

    override fun handleAction(aquarium: Aquarium, action: AquariumAdapter.ActionType) {
        when (action) {
            AquariumAdapter.ActionType.VIEW -> {
                view?.showToast("Viewing: ${aquarium.name}")
                view?.navigateToDetail(ParametersActivity::class.java, aquarium.id!!)
            }
            AquariumAdapter.ActionType.EDIT -> {
                view?.showEditDialog(aquarium)
            }
            AquariumAdapter.ActionType.DELETE -> {
                view?.showConfirmDeleteDialog(aquarium)
            }
        }
    }

    override fun handleNavigation(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> view?.navigateTo(HomeActivity::class.java)
            R.id.nav_aquarium -> { /* Stay on current activity */ }
            R.id.nav_notifications -> view?.navigateTo(NotificationActivity::class.java)
            R.id.nav_settings -> view?.navigateTo(SettingsActivity::class.java)
        }
    }
}