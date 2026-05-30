package com.example.hotel

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hotel.models.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "hotel_booking.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_HOTELS = "hotels"
        private const val TABLE_ROOMS = "rooms"
        private const val TABLE_CLIENTS = "clients"
        private const val TABLE_SERVICES = "services"
        private const val TABLE_BOOKINGS = "bookings"
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_HOTELS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                address TEXT NOT NULL,
                stars INTEGER DEFAULT 3,
                phone TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_ROOMS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hotel_id INTEGER NOT NULL,
                room_number TEXT NOT NULL,
                type TEXT NOT NULL,
                price_per_night REAL NOT NULL,
                is_available INTEGER DEFAULT 1,
                FOREIGN KEY(hotel_id) REFERENCES $TABLE_HOTELS(id) ON DELETE CASCADE
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_CLIENTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                passport_number TEXT NOT NULL,
                email TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_SERVICES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                cost REAL NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_BOOKINGS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                client_id INTEGER NOT NULL,
                room_id INTEGER NOT NULL,
                check_in_date TEXT NOT NULL,
                check_out_date TEXT NOT NULL,
                total_price REAL NOT NULL,
                FOREIGN KEY(client_id) REFERENCES $TABLE_CLIENTS(id) ON DELETE CASCADE,
                FOREIGN KEY(room_id) REFERENCES $TABLE_ROOMS(id) ON DELETE CASCADE
            )
        """)

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOTELS")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        val h1 = ContentValues().apply {
            put("name", "Grand Royal Hotel")
            put("address", "Kyiv, Khreshchatyk St, 12")
            put("stars", 5)
            put("phone", "+380441234567")
        }
        val h2 = ContentValues().apply {
            put("name", "Sea Breeze Resort")
            put("address", "Odesa, Primorsky Blvd, 5")
            put("stars", 4)
            put("phone", "+380487654321")
        }
        val h3 = ContentValues().apply {
            put("name", "Mountain Peak Lodge")
            put("address", "Carpathians, Yaremche, Svobody St, 45")
            put("stars", 3)
            put("phone", "+380341112233")
        }
        val id1 = db.insert(TABLE_HOTELS, null, h1)
        val id2 = db.insert(TABLE_HOTELS, null, h2)
        val id3 = db.insert(TABLE_HOTELS, null, h3)

        val rooms = listOf(
            ContentValues().apply {
                put("hotel_id", id1)
                put("room_number", "101")
                put("type", "Deluxe Double")
                put("price_per_night", 2500.0)
                put("is_available", 1)
            },
            ContentValues().apply {
                put("hotel_id", id1)
                put("room_number", "102")
                put("type", "Presidential Suite")
                put("price_per_night", 7500.0)
                put("is_available", 1)
            },
            ContentValues().apply {
                put("hotel_id", id2)
                put("room_number", "201")
                put("type", "Standard Single")
                put("price_per_night", 1200.0)
                put("is_available", 1)
            },
            ContentValues().apply {
                put("hotel_id", id2)
                put("room_number", "202")
                put("type", "Standard Double")
                put("price_per_night", 1800.0)
                put("is_available", 1)
            },
            ContentValues().apply {
                put("hotel_id", id3)
                put("room_number", "301")
                put("type", "Family Suite")
                put("price_per_night", 2200.0)
                put("is_available", 1)
            }
        )
        for (room in rooms) {
            db.insert(TABLE_ROOMS, null, room)
        }

        val clients = listOf(
            ContentValues().apply {
                put("full_name", "Ivan Kovalenko")
                put("passport_number", "AB123456")
                put("email", "ivan.koval@gmail.com")
            },
            ContentValues().apply {
                put("full_name", "Olena Shevchenko")
                put("passport_number", "CD789012")
                put("email", "olena.shev@ukr.net")
            },
            ContentValues().apply {
                put("full_name", "Dmitry Petrenko")
                put("passport_number", "EF345678")
                put("email", "dmitry.p@gmail.com")
            }
        )
        for (client in clients) {
            db.insert(TABLE_CLIENTS, null, client)
        }

        val services = listOf(
            ContentValues().apply {
                put("name", "Breakfast Buffet")
                put("cost", 300.0)
            },
            ContentValues().apply {
                put("name", "SPA Session")
                put("cost", 800.0)
            },
            ContentValues().apply {
                put("name", "Gym Pass")
                put("cost", 200.0)
            }
        )
        for (service in services) {
            db.insert(TABLE_SERVICES, null, service)
        }
    }

    fun getAllHotels(): List<Hotel> {
        val list = mutableListOf<Hotel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HOTELS", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Hotel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        stars = cursor.getInt(cursor.getColumnIndexOrThrow("stars")),
                        phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllRooms(hotelId: Int? = null): List<Room> {
        val list = mutableListOf<Room>()
        val db = readableDatabase
        val query = if (hotelId != null) {
            "SELECT * FROM $TABLE_ROOMS WHERE hotel_id = $hotelId"
        } else {
            "SELECT * FROM $TABLE_ROOMS"
        }
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Room(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        hotelId = cursor.getInt(cursor.getColumnIndexOrThrow("hotel_id")),
                        roomNumber = cursor.getString(cursor.getColumnIndexOrThrow("room_number")),
                        type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                        pricePerNight = cursor.getDouble(cursor.getColumnIndexOrThrow("price_per_night")),
                        isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow("is_available")) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllClients(): List<Client> {
        val list = mutableListOf<Client>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CLIENTS", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Client(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        passportNumber = cursor.getString(cursor.getColumnIndexOrThrow("passport_number")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email")) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getStats(): String {
        val db = readableDatabase
        var hotelsCount = 0
        var totalRooms = 0
        var availableRooms = 0
        var bookingsCount = 0
        var clientsCount = 0

        var cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_HOTELS", null)
        if (cursor.moveToFirst()) hotelsCount = cursor.getInt(0)
        cursor.close()

        cursor = db.rawQuery("SELECT COUNT(*), SUM(CASE WHEN is_available=1 THEN 1 ELSE 0 END) FROM $TABLE_ROOMS", null)
        if (cursor.moveToFirst()) {
            totalRooms = cursor.getInt(0)
            availableRooms = cursor.getInt(1)
        }
        cursor.close()

        cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_BOOKINGS", null)
        if (cursor.moveToFirst()) bookingsCount = cursor.getInt(0)
        cursor.close()

        cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CLIENTS", null)
        if (cursor.moveToFirst()) clientsCount = cursor.getInt(0)
        cursor.close()

        return "Hotels: $hotelsCount\nTotal Rooms: $totalRooms\nAvailable Rooms: $availableRooms\nBooked Rooms: ${totalRooms - availableRooms}\nActive Bookings: $bookingsCount\nRegistered Clients: $clientsCount"
    }

    fun insertBooking(clientId: Int, roomId: Int, checkInDate: String, checkOutDate: String, totalPrice: Double): Long {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("client_id", clientId)
                put("room_id", roomId)
                put("check_in_date", checkInDate)
                put("check_out_date", checkOutDate)
                put("total_price", totalPrice)
            }
            val id = db.insert(TABLE_BOOKINGS, null, values)
            if (id != -1L) {
                val updateValues = ContentValues().apply {
                    put("is_available", 0)
                }
                db.update(TABLE_ROOMS, updateValues, "id = ?", arrayOf(roomId.toString()))
            }
            db.setTransactionSuccessful()
            return id
        } finally {
            db.endTransaction()
        }
    }

    fun deleteBooking(bookingId: Int): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        try {
            var roomId = -1
            val cursor = db.rawQuery("SELECT room_id FROM $TABLE_BOOKINGS WHERE id = ?", arrayOf(bookingId.toString()))
            if (cursor.moveToFirst()) {
                roomId = cursor.getInt(0)
            }
            cursor.close()

            if (roomId != -1) {
                val deleted = db.delete(TABLE_BOOKINGS, "id = ?", arrayOf(bookingId.toString()))
                if (deleted > 0) {
                    val updateValues = ContentValues().apply {
                        put("is_available", 1)
                    }
                    db.update(TABLE_ROOMS, updateValues, "id = ?", arrayOf(roomId.toString()))
                }
                db.setTransactionSuccessful()
                return true
            }
            return false
        } finally {
            db.endTransaction()
        }
    }

    fun getDetailedBookingsList(): List<DetailedBooking> {
        val list = mutableListOf<DetailedBooking>()
        val db = readableDatabase
        val query = """
            SELECT 
                b.id AS booking_id,
                c.full_name AS client_name,
                h.name AS hotel_name,
                r.room_number AS room_number,
                b.check_in_date AS check_in,
                b.check_out_date AS check_out,
                b.total_price AS total_price
            FROM $TABLE_BOOKINGS b
            JOIN $TABLE_CLIENTS c ON b.client_id = c.id
            JOIN $TABLE_ROOMS r ON b.room_id = r.id
            JOIN $TABLE_HOTELS h ON r.hotel_id = h.id
        """
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    DetailedBooking(
                        bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("booking_id")),
                        clientName = cursor.getString(cursor.getColumnIndexOrThrow("client_name")),
                        hotelName = cursor.getString(cursor.getColumnIndexOrThrow("hotel_name")),
                        roomNumber = cursor.getString(cursor.getColumnIndexOrThrow("room_number")),
                        checkInDate = cursor.getString(cursor.getColumnIndexOrThrow("check_in")),
                        checkOutDate = cursor.getString(cursor.getColumnIndexOrThrow("check_out")),
                        totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}

data class DetailedBooking(
    val bookingId: Int,
    val clientName: String,
    val hotelName: String,
    val roomNumber: String,
    val checkInDate: String,
    val checkOutDate: String,
    val totalPrice: Double
)
