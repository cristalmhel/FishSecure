package com.example.fishsecure.helper

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisFormatter(private val labels: List<String>) : ValueFormatter() {

    /**
     * Called when a value from an axis is to be formatted into a human-readable string.
     * @param value The value to be formatted (this will be the float index: 0.0, 1.0, 2.0, etc.)
     */
//    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//        // Convert the float index to an integer index
//        val index = value.toInt()
//
//        // Ensure the index is within the bounds of the labels list
//        return if (index >= 0 && index < labels.size) {
//            labels[index]
//        } else {
//            // Return an empty string or default for any index outside your data range
//            ""
//        }
//    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        // Round the float to nearest integer safely
        val index = value.toInt()
        return labels.getOrNull(index) ?: "" // safely handles out-of-bounds
    }
}