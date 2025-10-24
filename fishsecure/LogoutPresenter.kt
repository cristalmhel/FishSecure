package com.example.fishsecure

class LogoutPresenter(
    private val view: LogoutContract.View
) : LogoutContract.Presenter {

    override fun onCancelClicked() {
        view.closeLogoutScreen()
    }

    override fun onLogoutClicked() {
        // Here you clear session/token/sharedPrefs etc.
        clearUserSession()

        // Then tell the View to navigate
        view.navigateToLogin()
    }

    private fun clearUserSession() {
        // Example: clear SharedPreferences session
        val prefs = view as? android.content.Context
        prefs?.getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
            ?.edit()
            ?.clear()
            ?.apply()
    }
}
