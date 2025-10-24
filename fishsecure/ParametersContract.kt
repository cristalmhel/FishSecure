package com.example.fishsecure

interface ParametersContract {

    interface View {
        fun showTemperature(value: String, isValid: Boolean)
        fun showPH(value: String, isValid: Boolean)
        fun showOxygen(value: String, isValid: Boolean)
        fun showStatus(value: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadParameters(aquariumId: Int)
        fun validateTemperature(temp: Double): Boolean
        fun validatePH(ph: Double): Boolean
        fun validateOxygen(oxygen: Double): Boolean
    }
}
