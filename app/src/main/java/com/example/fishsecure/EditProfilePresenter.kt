package com.example.fishsecure

class EditProfilePresenter(private val view: EditProfileContract.View) : EditProfileContract.Presenter {

    // Mock stored user data (replace with database or API later)
    private var firstName = "John"
    private var lastName = "Doe"
    private var email = "john.doe@email.com"

    override fun loadUserData() {
        view.fillUserData(firstName, lastName, email)
    }

    override fun onSaveClicked(firstName: String, lastName: String, email: String) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            view.showMessage("All fields are required")
            return
        }

        // Save updated values (mock update for now)
        this.firstName = firstName
        this.lastName = lastName
        this.email = email

        view.showMessage("Profile updated successfully")
        view.navigateBackToSettings()
    }
}
