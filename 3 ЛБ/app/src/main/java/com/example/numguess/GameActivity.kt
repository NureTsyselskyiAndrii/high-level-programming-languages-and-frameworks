package com.example.numguess

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var player: Player
    private lateinit var tvHint:     TextView
    private lateinit var tvAttempts: TextView
    private lateinit var tvResult:   TextView
    private lateinit var tvStats:    TextView
    private lateinit var etGuess:    EditText
    private lateinit var btnGuess:   Button
    private lateinit var layoutHistory: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        player      = AppData.players[AppData.currentUser]!!
        tvHint      = findViewById(R.id.tvHint)
        tvAttempts  = findViewById(R.id.tvAttempts)
        tvResult    = findViewById(R.id.tvResult)
        tvStats     = findViewById(R.id.tvStats)
        etGuess     = findViewById(R.id.etGuess)
        btnGuess    = findViewById(R.id.btnGuess)
        layoutHistory = findViewById(R.id.layoutHistory)

        findViewById<TextView>(R.id.tvUsername).text = "👤 ${player.username}"

        btnGuess.setOnClickListener { onGuess() }

        findViewById<Button>(R.id.btnNewGame).setOnClickListener {
            AppData.getOrCreate(AppData.currentUser)
            player = AppData.players[AppData.currentUser]!!
            layoutHistory.removeAllViews()
            tvHint.text = ""; tvResult.text = ""
            btnGuess.isEnabled = true
            etGuess.text.clear()
            updateUI()
        }

        findViewById<Button>(R.id.btnToLeaderboard).setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        updateUI()
    }

    private fun onGuess() {
        val input = etGuess.text.toString().toIntOrNull()
        etGuess.text.clear()

        if (input == null || input < 1 || input > 100) {
            Toast.makeText(this, "Введіть число від 1 до 100!",
                Toast.LENGTH_SHORT).show()
            return
        }

        player.attempts++

        val rowColor: Int
        val hintText: String

        when {
            input == player.currentSecret -> {
                player.wins++
                if (player.bestScore == null || player.attempts < player.bestScore!!)
                    player.bestScore = player.attempts
                tvResult.text = "🎉 Вгадав за ${player.attempts} спроб!"
                tvResult.setTextColor(Color.parseColor("#43A047"))
                btnGuess.isEnabled = false
                hintText = "✅ $input"
                rowColor = Color.parseColor("#E8F5E9")
            }
            input < player.currentSecret -> {
                tvHint.text = "Більше!"
                tvHint.setTextColor(Color.parseColor("#E53935"))
                hintText = "↑ $input — Більше!"
                rowColor = Color.parseColor("#FFEBEE")
            }
            else -> {
                tvHint.text = "Менше!"
                tvHint.setTextColor(Color.parseColor("#1E88E5"))
                hintText = "↓ $input — Менше!"
                rowColor = Color.parseColor("#E3F2FD")
            }
        }

        addHistoryRow(hintText, rowColor)
        updateUI()
    }

    private fun addHistoryRow(text: String, bgColor: Int) {
        val tv = TextView(this).apply {
            this.text = text
            textSize = 14f
            setBackgroundColor(bgColor)
            setPadding(16, 10, 16, 10)
            val p = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            p.setMargins(0, 0, 0, 4); layoutParams = p
        }
        layoutHistory.addView(tv, 0)
    }

    private fun updateUI() {
        tvAttempts.text = "Спроб: ${player.attempts}"
        val best = player.bestScore?.let { "Кращий: $it спроб" } ?: "—"
        tvStats.text = "Перемог: ${player.wins}  |  $best"
    }
}
