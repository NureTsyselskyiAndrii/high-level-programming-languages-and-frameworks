package com.example.hotel

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.hotel.models.Hotel
import com.example.hotel.models.Room

class RoomsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var spinnerHotel: Spinner
    private lateinit var layoutRooms: LinearLayout
    private var hotelsList = listOf<Hotel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        dbHelper = DatabaseHelper(this)
        spinnerHotel = findViewById(R.id.spinnerHotel)
        layoutRooms = findViewById(R.id.layoutRooms)

        loadHotels()

        spinnerHotel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedHotelId = if (position == 0) null else hotelsList[position - 1].id
                loadRooms(selectedHotelId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadHotels() {
        hotelsList = dbHelper.getAllHotels()
        val hotelNames = mutableListOf("All Hotels")
        hotelNames.addAll(hotelsList.map { "${it.name} (${it.stars}★)" })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hotelNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHotel.adapter = adapter
    }

    private fun loadRooms(hotelId: Int?) {
        layoutRooms.removeAllViews()
        val rooms = dbHelper.getAllRooms(hotelId)
        val hotelsMap = hotelsList.associateBy { it.id }

        if (rooms.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = "No rooms found."
                textSize = 16f
                setTextColor(Color.GRAY)
                setPadding(16, 16, 16, 16)
            }
            layoutRooms.addView(tvEmpty)
            return
        }

        for (room in rooms) {
            val cardView = CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 16)
                }
                radius = 12f
                cardElevation = 4f
                useCompatPadding = true
            }

            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 24, 24, 24)
            }

            val hotelName = hotelsMap[room.hotelId]?.name ?: "Unknown Hotel"

            val tvHotelName = TextView(this).apply {
                text = hotelName
                textSize = 14f
                setTextColor(Color.parseColor("#777777"))
            }

            val tvRoomDetails = TextView(this).apply {
                text = "Room ${room.roomNumber} - ${room.type}"
                textSize = 18f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(Color.parseColor("#333333"))
                setPadding(0, 4, 0, 4)
            }

            val tvPrice = TextView(this).apply {
                text = "Price: ${room.pricePerNight} UAH / night"
                textSize = 15f
                setTextColor(Color.parseColor("#6200EE"))
            }

            val tvStatus = TextView(this).apply {
                if (room.isAvailable) {
                    text = "Available"
                    setTextColor(Color.parseColor("#2E7D32"))
                } else {
                    text = "Booked"
                    setTextColor(Color.parseColor("#C62828"))
                }
                textSize = 15f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 4, 0, 0)
            }

            itemLayout.addView(tvHotelName)
            itemLayout.addView(tvRoomDetails)
            itemLayout.addView(tvPrice)
            itemLayout.addView(tvStatus)

            cardView.addView(itemLayout)
            layoutRooms.addView(cardView)
        }
    }
}
