package com.example.fishsecure.presenter

import com.example.fishsecure.contract.ParametersContract
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ParametersPresenter(private val view: ParametersContract.View) : ParametersContract.Presenter {

    private val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("aquariums")
    private val tempMin = 18.0
    private val tempMax = 30.0
    private val tdsMin = 100.0
    private val tdsMax = 3200.0
    private val ecMin = 0.150
    private val ecMax = 6.0
    override fun loadParameters(aquariumId: Int) {
        val singleAquariumRef = database.child(aquariumId.toString())

        // 3. Attach a listener to fetch the data once
        singleAquariumRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // The snapshot now contains only the data for "aquarium_id_1"
                if (snapshot.exists()) {
                    val temp = snapshot.child("temp").getValue(Double::class.java) ?: 0.0
                    val tds = snapshot.child("tds").getValue(Double::class.java) ?: 0.0
                    val ec = snapshot.child("ec").getValue(Double::class.java) ?: 0.0
                    val comment = snapshot.child("comment").getValue(String::class.java) ?: ""
                    val name = snapshot.child("name").getValue(String::class.java) ?: ""

                    println("Fetched Data for $aquariumId:")
                    println("Temp: $temp, TDS: $tds, EC: $ec")

                    val tempValid = validateTemperature(temp)
                    val tdsValid = validateTds(tds)
                    val ecValid = validateEc(ec)

                    val tempFormatted = "%.2f°C".format(temp)
                    val tdsFormatted = "%.2f ppm".format(tds)
                    val ecFormatted = "%.3f mS/cm".format(ec)
                    view.showTitle(name)
                    view.showTemperature(tempFormatted, tempValid)
                    view.showTds(tdsFormatted, tdsValid)
                    view.showEc(ecFormatted, ecValid)
                    view.showComment(comment)

                    // Status logic
                    val status = when {
                        !tempValid && temp < tempMin -> "Temperature is LOW"
                        !tempValid && temp > tempMax -> "Temperature is HIGH"
                        !tdsValid && tds < tdsMin -> "TDS is LOW"
                        !tdsValid && tds > tdsMax -> "TDS is HIGH"
                        !ecValid && ec < ecMin -> "EC level is LOW"
                        !ecValid && ec > ecMax -> "EC level is HIGH"
                        tempValid && tdsValid && ecValid -> "Normal"
                        else -> "Parameters need attention"
                    }
                    view.showStatus(status)
                } else {
                    println("Aquarium ID: $aquariumId not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                println("Database error: ${error.message}")
            }
        })
    }

    override fun validateTemperature(temp: Double): Boolean {
        return temp in tempMin..tempMax
    }

    override fun validateTds(tds: Double): Boolean {
        // Validates TDS in ppm (parts per million)
        return tds in tdsMin..tdsMax
    }

    override fun validateEc(ec: Double): Boolean {
        // Validates EC in µS/cm (microsiemens per centimeter)
        // Note: The input value 'ec' must be in µS/cm, not just a small unit like 5.0
        return ec in ecMin..ecMax
    }
}
