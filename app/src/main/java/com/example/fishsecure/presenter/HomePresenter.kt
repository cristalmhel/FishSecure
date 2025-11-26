package com.example.fishsecure.presenter

import com.example.fishsecure.contract.HomeContract
import com.example.fishsecure.model.Aquarium
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class HomePresenter(private var view: HomeContract.View?) : HomeContract.Presenter {
    // Direct reference to the Firebase Realtime Database
    private val rootRef = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("aquariums")

    // --- HomeContract.Presenter Methods ---

    override fun getAquariumDetails(username: String) {
        if (username.isBlank()) {
            view?.showErrorMessage("Username is required to fetch data.")
            return
        }

        view?.showLoading()

        // 1. Construct the Query: Order by 'user' and look for an exact match.
        val query = rootRef
            .orderByChild("user")
            .equalTo(username)

        // Fetch data once
        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val activeAquariums = mutableListOf<Aquarium>()

                // 2. Client-Side Filtering: Iterate results to find the active aquarium.
                for (aquariumSnapshot in snapshot.children) {

                    val status = aquariumSnapshot.child("status").getValue(String::class.java) ?: ""

                    // In your presenter's onDataChange method, inside the loop:

                    if (status.isNotEmpty() && status != "Inactive") {

                        // --- Safe Extraction using Elvis Operator (for non-critical/optional fields) ---
                        // Provide a default value (e.g., null, 0, or empty string) if the child doesn't exist or is null.
                        val id = aquariumSnapshot.child("id").getValue(Int::class.java)
                        val description =
                            aquariumSnapshot.child("description").getValue(String::class.java) ?: ""
                        val comment =
                            aquariumSnapshot.child("comment").getValue(String::class.java) ?: ""
                        val user = aquariumSnapshot.child("user").getValue(String::class.java) ?: ""

                        // --- Critical Data Extraction ---
                        // These must be non-null to create the Aquarium object.
                        val name = aquariumSnapshot.child("name").getValue(String::class.java)
                        val ec = aquariumSnapshot.child("ec").getValue(Float::class.java)
                        val temp = aquariumSnapshot.child("temp").getValue(Float::class.java)
                        val tds = aquariumSnapshot.child("tds").getValue(Float::class.java)

                        // --- Final Check (Smart Cast) ---
                        // Use the !! assertion or a null check only on the critical, non-nullable fields.
                        if (name != null && temp != null && tds != null && ec != null) {

                            // Create the Aquarium object
                            val aquariumData = Aquarium(
                                id = id,
                                name = name,
                                description = description,
                                status = status,
                                comment = comment,
                                user = user,
                                ec = ec,
                                temp = temp,
                                tds = tds
                            )
                            // Add it to the list instead of breaking
                            activeAquariums.add(aquariumData)
                        }
                    }
                }

                // 3. Update the View
                view?.hideLoading()
                if (activeAquariums.isNotEmpty()) {
                    // Change the view method signature to accept a List
                    view?.displayAquariumData(activeAquariums)
                } else {
                    view?.showErrorMessage("No active aquariums found for user: $username")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                view?.hideLoading()
                view?.showErrorMessage("Firebase error: ${error.message}")
            }
        })
    }

    override fun onDestroy() {
        // Prevents memory leaks by releasing the reference to the View
        this.view = null
    }
}
