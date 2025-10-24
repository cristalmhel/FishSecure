package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class SplashScreenActivity : Activity() {
    private lateinit var getStartedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        getStartedButton = findViewById(R.id.btnGetStarted)

        // If your DB is in a non-default region, pass the DB url like:
//        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
////        val database = Firebase.database
//        val myRef = database.getReference("message")
//
//        // Write a value
//        myRef.setValue("Hello from Android!")
//        // Read from the database
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue<String>(String::class.java)
//                Log.d(TAG, "Value is: $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//        })

        getStartedButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}