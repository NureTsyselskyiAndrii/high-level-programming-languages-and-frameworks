package com.example.numguess

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val layout = findViewById<LinearLayout>(R.id.layoutRows)
        val board  = AppData.getLeaderboard()
        val medals = listOf("🥇", "🥈", "🥉")

        if (board.isEmpty()) {
            layout.addView(TextView(this).apply {
                text = "Поки немає результатів. Зіграй першим!"
                setTextColor(Color.GRAY); textSize = 14f
                setPadding(16, 24, 16, 0)
            })
        }

        board.forEachIndexed { i, player ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(12, 14, 12, 14)
                setBackgroundColor(
                    if (i % 2 == 0) Color.WHITE
                    else Color.parseColor("#F9F8FF"))
                val p = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                p.setMargins(0, 0, 0, 2); layoutParams = p
            }

            fun cell(text: String, weight: Float = 0f,
                     width: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
                     color: String = "#333333",
                     bold: Boolean = false) = TextView(this).apply {
                this.text = text
                textSize = 14f
                setTextColor(Color.parseColor(color))
                if (bold) setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(width,
                    LinearLayout.LayoutParams.WRAP_CONTENT, weight).also {
                    it.gravity = android.view.Gravity.CENTER_VERTICAL
                }
            }

            row.addView(cell(if (i < 3) medals[i] else "${i+1}",
                width = 40.dpToPx()))
            row.addView(cell(player.username, weight = 1f, bold = true))
            row.addView(cell("${player.wins}",
                width = 80.dpToPx(), color = "#22C55E", bold = true))
            row.addView(cell(player.bestScore?.let { "$it сп." } ?: "—",
                width = 80.dpToPx(), color = "#1E88E5"))

            layout.addView(row)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun Int.dpToPx(): Int =
        (this * resources.displayMetrics.density).toInt()
}
