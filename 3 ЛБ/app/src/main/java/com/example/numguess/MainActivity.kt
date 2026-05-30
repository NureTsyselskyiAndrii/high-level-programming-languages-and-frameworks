package com.example.numguess

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername      = findViewById<EditText>(R.id.etUsername)
        val btnPlay         = findViewById<Button>(R.id.btnPlay)
        val btnLeaderboard  = findViewById<Button>(R.id.btnLeaderboard)

        btnPlay.setOnClickListener {
            val name = etUsername.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Введіть нікнейм!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AppData.currentUser = name
            AppData.getOrCreate(name)
            startActivity(Intent(this, GameActivity::class.java))
        }

        btnLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }
    }
}
