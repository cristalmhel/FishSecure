package com.example.fishsecure

import com.example.fishsecure.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.database

class LoginPresenter(private val view: LoginView) {

    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.onLoginError("Username and password cannot be empty")
            return
        }

        checkUser(username, password)
    }
    private fun checkUser(usernameInput: String, passwordInput: String) {
        // Go to Users > username
        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

        database.child(usernameInput).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val passwordFromDB = snapshot.child("password").value.toString()

                if (passwordFromDB == passwordInput) {
                    val firstName = snapshot.child("firstName").value.toString()
                    val lastName = snapshot.child("lastName").value.toString()
                    val middleName = snapshot.child("middleName").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val email = snapshot.child("email").value.toString()
                    val password = snapshot.child("password").value.toString()

                    // Create a User object
                    val user = User(
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        username = username,
                        email = email,
                        password = password
                    )

                    // Store globally
                    UserManager.currentUser = user
                    view.onLoginSuccess("Login successful! Welcome, $usernameInput.")
                } else {
                    view.onLoginError("Invalid username or password")
                }

            } else {
                view.onLoginError("User not found")
            }
        }.addOnFailureListener {
            view.onLoginError("Failed to read data: ${it.message}")
        }
    }


}
