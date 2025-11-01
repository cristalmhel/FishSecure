package com.example.fishsecure

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.fishsecure.contract.HomeContract
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

//    private lateinit var addNewBtn: TextView
//    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var adapter: AquariumAdapter
    private lateinit var greetingText: TextView
    private lateinit var dateText: TextView

    // Define chart views
    private lateinit var temperatureChart: LineChart
    private lateinit var tdsChart: LineChart
    private lateinit var ecChart: LineChart

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        addNewBtn = findViewById(R.id.addNewBtn)
//        recyclerView = findViewById(R.id.aquariumRecyclerView)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        greetingText = findViewById(R.id.greetingText)
        dateText = findViewById(R.id.dateText)

        presenter = HomePresenter(this)

//        adapter = AquariumAdapter(
//            emptyList(),
//            onDeleteClick = { id -> presenter.deleteAquarium(id) },
//            onRenameClick = { id -> showRenameDialog(id) }
//        )

        // Initialize chart views
        temperatureChart = findViewById(R.id.temperatureChart)
        tdsChart = findViewById(R.id.tdsChart)
        ecChart = findViewById(R.id.ecChart)

        // Setup and populate the charts
        setupChart(temperatureChart, getTemperatureData(), "Temperature Data", "#FF5722") // Example Color: Deep Orange
        setupChart(tdsChart, getTDSData(), "TDS Data", "#4CAF50") // Example Color: Green
        setupChart(ecChart, getECData(), "EC Data", "#2196F3") // Example Color: Blue

//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//
//        addNewBtn.setOnClickListener {
//            presenter.addAquarium()
//        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { true }
                R.id.nav_aquarium -> { navigateTo(AquariumActivity::class.java); true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }

        val user = UserManager.currentUser
        val firstName = user?.firstName
        greetingText.text = if (!firstName.isNullOrEmpty()) {
            "Good Day, $firstName!"
        } else {
            "Welcome!"
        }

        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())
        dateText.text = dateFormat.format(Date())
    }

    override fun showAquariums(aquariums: List<com.example.fishsecure.model.Aquarium>) {
//        adapter.updateData(aquariums)
    }

    private fun showRenameDialog(aquariumId: Int) {
        val input = EditText(this)
        input.hint = "Enter new name"

        AlertDialog.Builder(this)
            .setTitle("Rename Aquarium")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    presenter.renameAquarium(aquariumId, newName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
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
    private fun setupChart(chart: LineChart, entries: List<Entry>, label: String, colorHex: String) {
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

    // --- Sample Data Functions ---

    // In your Activity/Fragment class, define your string list
    private val xLabels = listOf(
        "Aquarium 1",
    )

    // ✅ Data
    private fun getTemperatureData(): List<Entry> {
        return listOf(
            Entry(0f, 25.5f),
        )
    }

    private fun getTDSData(): List<Entry> {
        return listOf(
            Entry(0f, 150f),
        )
    }

    private fun getECData(): List<Entry> {
        return listOf(
            Entry(0f, 300f),
        )
    }
}
