package com.example.fishsecure

class ParametersPresenter(private val view: ParametersContract.View) : ParametersContract.Presenter {

    override fun loadParameters(aquariumId: Int) {
        // Normally, fetch from Repository / API / DB.
        // Here we use mock values for demo:
        val temp = 27.0
        val ph = 7.8
        val oxygen = 6.0

        val tempValid = validateTemperature(temp)
        val phValid = validatePH(ph)
        val oxygenValid = validateOxygen(oxygen)

        view.showTemperature("$temp°C", tempValid)
        view.showPH(ph.toString(), phValid)
        view.showOxygen("$oxygen mg/L", oxygenValid)

        // Status logic
        val status = if (tempValid && phValid && oxygenValid) "Normal" else "Check Parameters!"
        view.showStatus(status)
    }

    override fun validateTemperature(temp: Double): Boolean {
        return temp in 24.0..28.0
    }

    override fun validatePH(ph: Double): Boolean {
        return ph in 6.5..8.0
    }

    override fun validateOxygen(oxygen: Double): Boolean {
        return oxygen in 5.0..8.0
    }
}
