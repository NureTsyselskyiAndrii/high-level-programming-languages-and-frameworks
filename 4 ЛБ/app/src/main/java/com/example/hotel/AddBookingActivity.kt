package com.example.hotel

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hotel.models.Client
import com.example.hotel.models.Room
import com.example.hotel.models.Hotel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class AddBookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var spinnerClient: Spinner
    private lateinit var spinnerRoom: Spinner
    private lateinit var etCheckIn: EditText
    private lateinit var etCheckOut: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button

    private var clientsList = listOf<Client>()
    private var roomsList = listOf<Room>()
    private var hotelsList = listOf<Hotel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_booking)

        dbHelper = DatabaseHelper(this)
        spinnerClient = findViewById(R.id.spinnerClient)
        spinnerRoom = findViewById(R.id.spinnerRoom)
        etCheckIn = findViewById(R.id.etCheckIn)
        etCheckOut = findViewById(R.id.etCheckOut)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        loadClients()
        loadAvailableRooms()

        btnSave.setOnClickListener {
            saveBooking()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadClients() {
        clientsList = dbHelper.getAllClients()
        val clientNames = clientsList.map { "${it.fullName} (${it.passportNumber})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClient.adapter = adapter
    }

    private fun loadAvailableRooms() {
        hotelsList = dbHelper.getAllHotels()
        val hotelsMap = hotelsList.associateBy { it.id }

        roomsList = dbHelper.getAllRooms().filter { it.isAvailable }
        val roomDetails = roomsList.map { room ->
            val hotelName = hotelsMap[room.hotelId]?.name ?: "Unknown"
            "$hotelName - Room ${room.roomNumber} (${room.type}) - ${room.pricePerNight} UAH"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roomDetails)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRoom.adapter = adapter
    }

    private fun saveBooking() {
        if (clientsList.isEmpty()) {
            Toast.makeText(this, "No clients registered.", Toast.LENGTH_SHORT).show()
            return
        }
        if (roomsList.isEmpty()) {
            Toast.makeText(this, "No available rooms.", Toast.LENGTH_SHORT).show()
            return
        }

        val clientPos = spinnerClient.selectedItemPosition
        val roomPos = spinnerRoom.selectedItemPosition

        if (clientPos < 0 || roomPos < 0) {
            Toast.makeText(this, "Please select client and room.", Toast.LENGTH_SHORT).show()
            return
        }

        val client = clientsList[clientPos]
        val room = roomsList[roomPos]

        val checkInStr = etCheckIn.text.toString().trim()
        val checkOutStr = etCheckOut.text.toString().trim()

        if (checkInStr.isEmpty() || checkOutStr.isEmpty()) {
            Toast.makeText(this, "Please enter check-in and check-out dates.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
        val checkInDate = try {
            sdf.parse(checkInStr)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid check-in date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show()
            return
        }

        val checkOutDate = try {
            sdf.parse(checkOutStr)
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid check-out date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show()
            return
        }

        if (checkOutDate != null && checkInDate != null && !checkOutDate.after(checkInDate)) {
            Toast.makeText(this, "Check-out date must be after check-in date.", Toast.LENGTH_SHORT).show()
            return
        }

        val diffInMillis = checkOutDate.time - checkInDate.time
        val nights = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
        val totalPrice = nights * room.pricePerNight

        val id = dbHelper.insertBooking(client.id, room.id, checkInStr, checkOutStr, totalPrice)
        if (id != -1L) {
            Toast.makeText(this, "Room booked successfully! Total: $totalPrice UAH", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Booking failed.", Toast.LENGTH_SHORT).show()
        }
    }
}
