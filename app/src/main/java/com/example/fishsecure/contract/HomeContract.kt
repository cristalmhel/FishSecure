package com.example.fishsecure.contract

import com.example.fishsecure.model.Aquarium

interface HomeContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        // Method to display the fetched sensor data and aquarium name
        fun displayAquariumData(aquariumList: List<Aquarium>)
        fun showErrorMessage(message: String)
    }

    interface Presenter {
        // Method for the View to request data based on the user's username
        fun getAquariumDetails(username: String)
        fun onDestroy() // For cleanup
    }
}
