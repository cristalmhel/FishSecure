package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.fishsecure.contract.HomeContract
import com.example.fishsecure.model.Aquarium
import com.example.fishsecure.presenter.HomePresenter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : Activity(), HomeContract.View {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var greetingText: TextView
    private lateinit var dateText: TextView
    private lateinit var loadingProgressBar: ProgressBar

    // Define chart views
    private lateinit var temperatureChart: LineChart
    private lateinit var tdsChart: LineChart
    private lateinit var ecChart: LineChart

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        greetingText = findViewById(R.id.greetingText)
        dateText = findViewById(R.id.dateText)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        // Initialize chart views
        temperatureChart = findViewById(R.id.temperatureChart)
        tdsChart = findViewById(R.id.tdsChart)
        ecChart = findViewById(R.id.ecChart)

        presenter = HomePresenter(this)
        val user = UserManager.currentUser
        val username = user?.username

        if (!username.isNullOrEmpty()) {
            presenter.getAquariumDetails(username)
        } else {
            showErrorMessage("User not logged in or username is missing.")
        }

//        // Setup and populate the charts
//        setupChart(temperatureChart, getTemperatureData(), "Temperature Data", "#FF5722") // Example Color: Deep Orange
//        setupChart(tdsChart, getTDSData(), "TDS Data", "#4CAF50") // Example Color: Green
//        setupChart(ecChart, getECData(), "EC Data", "#2196F3") // Example Color: Blue

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { true }
                R.id.nav_aquarium -> { navigateTo(AquariumActivity::class.java); true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }

        val firstName = user?.firstName
        greetingText.text = if (!firstName.isNullOrEmpty()) {
            "Good Day, $firstName!"
        } else {
            "Welcome!"
        }

        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())
        dateText.text = dateFormat.format(Date())
    }

    override fun onDestroy() {
        presenter.onDestroy() // Call to detach the view from the presenter
        super.onDestroy()
    }

    // --- HomeContract.View Implementations ---

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun displayAquariumData(aquariumList: List<Aquarium>) {
        if (aquariumList.isEmpty()) {
            // Handle this case if necessary, though the presenter should handle it.
            return
        }

        // Get the first aquarium's data to display on the charts
//        val aquariumData = aquariumList.first()
//
//        // Create the list of X-axis labels dynamically
//        val dynamicXLabels = listOf(aquariumData.name)
//
//        // 1. Create entry lists from the fetched data
//        val tempEntries = listOf(Entry(0f, aquariumData.temp))
//        val tdsEntries = listOf(Entry(0f, aquariumData.tds))
//        val ecEntries = listOf(Entry(0f, aquariumData.ec))

        // --- 1. Initialize lists to hold data for ALL aquariums ---
        // These lists will store the collected entries and labels for the charts.
        val dynamicXLabels = mutableListOf<String>()

        // We need to group all temperature values, all TDS values, and all EC values.
        val tempEntries = mutableListOf<Entry>()
        val tdsEntries = mutableListOf<Entry>()
        val ecEntries = mutableListOf<Entry>()

        // --- 2. Iterate and collect data from the list ---
        // The index (i) will serve as the X-value for positioning on the chart.
        aquariumList.forEachIndexed { index, aquarium ->

            // Add the aquarium name for the X-axis label
            dynamicXLabels.add(aquarium.name)

            // Create an Entry for each metric, using the index as the X-position (0f, 1f, 2f, etc.)
            val xPos = index.toFloat()

            tempEntries.add(Entry(xPos, aquarium.temp))
            tdsEntries.add(Entry(xPos, aquarium.tds))
            ecEntries.add(Entry(xPos, aquarium.ec))
        }

        // --- 3. Setup and update the charts with the collected data ---
        // The setupChart method must be able to handle this list of entries now.
        setupChart(temperatureChart, tempEntries, "Temperature (°C)", "#FF5722", dynamicXLabels)
        setupChart(tdsChart, tdsEntries, "TDS (ppm)", "#4CAF50", dynamicXLabels)
        setupChart(ecChart, ecEntries, "EC (µS/cm)", "#2196F3", dynamicXLabels)
    }

    override fun showErrorMessage(message: String) {
        hideLoading()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Helper
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }

    /**
     * Common function to configure and populate a LineChart
     */
    private fun setupChart(chart: LineChart, entries: List<Entry>, label: String, colorHex: String, xLabels: List<String>) {
        val dataSet = LineDataSet(entries, label).apply {
            color = Color.parseColor(colorHex)
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setCircleColor(Color.parseColor(colorHex))
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        chart.data = LineData(dataSet)

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        // ✅ This is the correct formatter for label lists
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

        // ✅ Set limits so MPAndroidChart knows the range
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = xLabels.size - 0.5f

        xAxis.labelRotationAngle = -45f
        xAxis.labelCount = xLabels.size
        xAxis.setTextSize(9f)
        xAxis.textColor = Color.BLACK

        // Y-axis
        chart.axisRight.isEnabled = false
        chart.axisLeft.setDrawGridLines(true)
        chart.axisLeft.textColor = Color.BLACK

        // General chart config
        chart.description.isEnabled = false
        chart.legend.isEnabled = true
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        chart.invalidate()
    }
}
