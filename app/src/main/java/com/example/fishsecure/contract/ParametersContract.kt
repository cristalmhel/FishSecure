package com.example.fishsecure.contract

interface ParametersContract {

    interface View {
        fun showTemperature(value: String, isValid: Boolean)
        fun showTds(value: String, isValid: Boolean)
        fun showEc(value: String, isValid: Boolean)
        fun showComment(value: String)
        fun showTitle(value: String)
        fun showStatus(value: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadParameters(aquariumId: Int)
        fun validateTemperature(temp: Double): Boolean
        fun validateTds(tds: Double): Boolean
        fun validateEc(ec: Double): Boolean
    }
}
