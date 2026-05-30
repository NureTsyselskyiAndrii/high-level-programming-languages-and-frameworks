package com.example.hotel

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class BookingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var layoutBookings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        dbHelper = DatabaseHelper(this)
        layoutBookings = findViewById(R.id.layoutBookings)

        loadBookings()
    }

    private fun loadBookings() {
        layoutBookings.removeAllViews()
        val bookings = dbHelper.getDetailedBookingsList()

        if (bookings.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = "No bookings found."
                textSize = 16f
                setTextColor(Color.GRAY)
                setPadding(16, 16, 16, 16)
            }
            layoutBookings.addView(tvEmpty)
            return
        }

        for (booking in bookings) {
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

            val tvHotelRoom = TextView(this).apply {
                text = "${booking.hotelName} - Room ${booking.roomNumber}"
                textSize = 18f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(Color.parseColor("#333333"))
            }

            val tvClient = TextView(this).apply {
                text = "Client: ${booking.clientName}"
                textSize = 15f
                setTextColor(Color.parseColor("#555555"))
                setPadding(0, 4, 0, 4)
            }

            val tvDates = TextView(this).apply {
                text = "Dates: ${booking.checkInDate} to ${booking.checkOutDate}"
                textSize = 14f
                setTextColor(Color.parseColor("#777777"))
            }

            val tvPrice = TextView(this).apply {
                text = "Total Price: ${booking.totalPrice} UAH"
                textSize = 16f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(Color.parseColor("#018786"))
                setPadding(0, 6, 0, 12)
            }

            val btnCancel = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Cancel Booking"
                textSize = 14f
                setAllCaps(false)
                setTextColor(Color.WHITE)
                backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#C62828"))
                setOnClickListener {
                    cancelBooking(booking.bookingId)
                }
            }

            itemLayout.addView(tvHotelRoom)
            itemLayout.addView(tvClient)
            itemLayout.addView(tvDates)
            itemLayout.addView(tvPrice)
            itemLayout.addView(btnCancel)

            cardView.addView(itemLayout)
            layoutBookings.addView(cardView)
        }
    }

    private fun cancelBooking(bookingId: Int) {
        val success = dbHelper.deleteBooking(bookingId)
        if (success) {
            Toast.makeText(this, "Booking cancelled and room is now available.", Toast.LENGTH_SHORT).show()
            loadBookings()
        } else {
            Toast.makeText(this, "Failed to cancel booking.", Toast.LENGTH_SHORT).show()
        }
    }
}
