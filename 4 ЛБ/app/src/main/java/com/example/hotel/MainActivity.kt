package com.example.hotel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvStats: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        tvStats = findViewById(R.id.tvStats)

        findViewById<Button>(R.id.btnRooms).setOnClickListener {
            startActivity(Intent(this, RoomsActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddBooking).setOnClickListener {
            startActivity(Intent(this, AddBookingActivity::class.java))
        }

        findViewById<Button>(R.id.btnBookings).setOnClickListener {
            startActivity(Intent(this, BookingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        tvStats.text = dbHelper.getStats()
    }
}
