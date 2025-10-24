package com.example.fishsecure

class EditProfilePresenter(private val view: EditProfileContract.View) : EditProfileContract.Presenter {

    // Mock stored user data (replace with database or API later)
    private var firstName = "John"
    private var lastName = "Doe"
    private var username = "john doe"
    private var email = "john.doe@email.com"

    override fun loadUserData() {
        view.fillUserData(firstName, lastName, username, email)
    }

    override fun onSaveClicked(firstName: String, lastName: String, username: String, email: String) {
        if (firstName.isBlank() || lastName.isBlank() || username.isBlank() || email.isBlank()) {
            view.showMessage("All fields are required")
            return
        }

        // Basic username validation
        if (username.length < 3) {
            view.showMessage("Username must be at least 3 characters")
            return
        }

        if (!username.matches("^[a-zA-Z0-9_]+$".toRegex())) {
            view.showMessage("Username can only contain letters, numbers, and underscores")
            return
        }

        // Save updated values (mock update for now)
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.email = email

        view.showMessage("Profile updated successfully")
        view.navigateBackToSettings()
    }
}