package com.example.multiply

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    data class WordEntry(
        val word: String,
        val hint: String,
        val emoji: String
    )

    private val wordList = listOf(
        WordEntry("СОНЦЕ",    "Зірка нашої сонячної системи",     "☀"),
        WordEntry("ОКЕАН",    "Найбільший водний простір Землі",   "🌊"),
        WordEntry("ГОРА",     "Найвища точка рельєфу",             "⛰"),
        WordEntry("ЛІCОВИЙ",  "Пов'язаний з деревами та рослинами","🌲"),
        WordEntry("ЯБЛУКО",   "Червоний або зелений фрукт",        "🍎"),
        WordEntry("ПОЛУНИЦЯ", "Солодка червона ягода",             "🍓"),
        WordEntry("ХЛІБ",     "Основний продукт з борошна",        "🍞"),
        WordEntry("МАНДРІВКА","Подорож до нових місць",            "🗺"),
        WordEntry("ВОГОНЬ",   "Стихія тепла та світла",            "🔥"),
        WordEntry("ЗІРКА",    "Світиться вночі на небі",           "⭐"),
    )

    private lateinit var currentEntry: WordEntry
    private var guessedLetters = mutableSetOf<Char>()
    private var attemptsLeft   = 7
    private var score          = 0

    private lateinit var tvEmoji:      TextView
    private lateinit var tvHint:       TextView
    private lateinit var tvWord:       TextView
    private lateinit var tvAttempts:   TextView
    private lateinit var tvScore:      TextView
    private lateinit var tvUsed:       TextView
    private lateinit var tvResult:     TextView
    private lateinit var etLetter:     EditText
    private lateinit var btnGuess:     Button
    private lateinit var btnNewGame:   Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvEmoji    = findViewById(R.id.tvEmoji)
        tvHint     = findViewById(R.id.tvHint)
        tvWord     = findViewById(R.id.tvWord)
        tvAttempts = findViewById(R.id.tvAttempts)
        tvScore    = findViewById(R.id.tvScore)
        tvUsed     = findViewById(R.id.tvUsed)
        tvResult   = findViewById(R.id.tvResult)
        etLetter   = findViewById(R.id.etLetter)
        btnGuess   = findViewById(R.id.btnGuess)
        btnNewGame = findViewById(R.id.btnNewGame)

        btnGuess.setOnClickListener   { onGuess() }
        btnNewGame.setOnClickListener { startGame() }

        startGame()
    }

    private fun startGame() {
        currentEntry   = wordList.random()
        guessedLetters = mutableSetOf()
        attemptsLeft   = 7
        tvResult.text  = ""
        btnGuess.isEnabled = true
        etLetter.text.clear()
        updateUI()
    }

    private fun onGuess() {
        val input = etLetter.text.toString().uppercase().trim()
        etLetter.text.clear()

        if (input.isEmpty() || input.length > 1) {
            Toast.makeText(this, "Введіть одну букву!", Toast.LENGTH_SHORT).show()
            return
        }

        val letter = input[0]
        if (letter in guessedLetters) {
            Toast.makeText(this, "Букву «$letter» вже вводили!", Toast.LENGTH_SHORT).show()
            return
        }

        guessedLetters.add(letter)
        if (letter !in currentEntry.word) attemptsLeft--

        updateUI()
        checkGameOver()
    }

    private fun buildWordDisplay(): String =
        currentEntry.word.map { if (it in guessedLetters) it else '_' }
            .joinToString("  ")

    private fun buildHangman(): String {
        val stages = listOf("", "╷", "╷\n│", "╷\n│\n○", "╷\n│\n○\n│",
                            "╷\n│\n○\n/│", "╷\n│\n○\n/│\\", "╷\n│\n○\n/│\\\n/ \\")
        return stages[7 - attemptsLeft]
    }

    private fun updateUI() {
        tvEmoji.text    = currentEntry.emoji
        tvHint.text     = "Підказка: ${currentEntry.hint}"
        tvWord.text     = buildWordDisplay()
        tvAttempts.text = "Залишилось помилок: $attemptsLeft  ${buildHangman()}"
        tvScore.text    = "Очки: $score"
        tvUsed.text     = if (guessedLetters.isEmpty()) ""
                          else "Введені: ${guessedLetters.sorted().joinToString(" ")}"
    }

    private fun checkGameOver() {
        val allGuessed = currentEntry.word.all { it in guessedLetters }
        when {
            allGuessed -> {
                score += attemptsLeft * 10
                tvResult.text = "🎉 Слово вгадано! +${attemptsLeft * 10} очок"
                tvResult.setTextColor(Color.parseColor("#43A047"))
                tvWord.text = currentEntry.word.toList().joinToString("  ")
                btnGuess.isEnabled = false
            }
            attemptsLeft == 0 -> {
                tvResult.text = "😢 Програш! Слово: ${currentEntry.word}"
                tvResult.setTextColor(Color.parseColor("#E53935"))
                tvWord.text = currentEntry.word.toList().joinToString("  ")
                btnGuess.isEnabled = false
            }
        }
    }
}
