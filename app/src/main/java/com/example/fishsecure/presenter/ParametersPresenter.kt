package com.example.fishsecure.presenter

import com.example.fishsecure.contract.ParametersContract

class ParametersPresenter(private val view: ParametersContract.View) : ParametersContract.Presenter {

    override fun loadParameters(aquariumId: Int) {
        // Normally, fetch from Repository / API / DB.
        // Here we use mock values for demo:
        val temp = 27.0
        val tds = 150.0
        val ec = 400.0

        val tempValid = validateTemperature(temp)
        val phValid = validateTds(tds)
        val oxygenValid = validateEc(ec)

        view.showTitle("Aquarium $aquariumId")
        view.showTemperature("$temp°C", tempValid)
        view.showTds("$tds ppm", phValid)
        view.showEc("$ec µS/cm", oxygenValid)
        view.showComment("This fish aquarium is for Koi")

        // Status logic
        val status = if (tempValid && phValid && oxygenValid) "Normal" else "Check Parameters!"
        view.showStatus(status)
    }

    override fun validateTemperature(temp: Double): Boolean {
        return temp in 18.0..28.0
    }

    override fun validateTds(tds: Double): Boolean {
        // Validates TDS in ppm (parts per million)
        return tds in 150.0..500.0
    }

    override fun validateEc(ec: Double): Boolean {
        // Validates EC in µS/cm (microsiemens per centimeter)
        // Note: The input value 'ec' must be in µS/cm, not just a small unit like 5.0
        return ec in 400.0..1200.0
    }
}
