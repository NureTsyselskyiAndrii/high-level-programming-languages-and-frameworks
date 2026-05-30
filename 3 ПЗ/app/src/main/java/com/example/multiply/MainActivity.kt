package com.example.multiply

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNum1    = findViewById<EditText>(R.id.etNum1)
        val etNum2    = findViewById<EditText>(R.id.etNum2)
        val btnCalc   = findViewById<Button>(R.id.btnCalc)
        val tvResult  = findViewById<TextView>(R.id.tvResult)
        val tvFormula = findViewById<TextView>(R.id.tvFormula)

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val a = etNum1.text.toString()
                val b = etNum2.text.toString()
                if (a.isNotEmpty() && b.isNotEmpty()) {
                    tvFormula.text = "$a × $b = ?"
                } else {
                    tvFormula.text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        }
        etNum1.addTextChangedListener(watcher)
        etNum2.addTextChangedListener(watcher)

        btnCalc.setOnClickListener {
            val a = etNum1.text.toString().toDoubleOrNull()
            val b = etNum2.text.toString().toDoubleOrNull()

            when {
                a == null || b == null -> {
                    tvResult.text = "❌ Введіть коректні числа"
                    tvResult.setTextColor(android.graphics.Color.RED)
                }
                else -> {
                    val result = a * b
                    val formatted = if (result == result.toLong().toDouble())
                        result.toLong().toString()
                    else "%.4f".format(result)
                    tvResult.text = "= $formatted"
                    tvResult.setTextColor(android.graphics.Color.parseColor("#6200EE"))
                    tvFormula.text = "$a × $b"
                }
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            etNum1.text.clear(); etNum2.text.clear()
            tvResult.text = ""; tvFormula.text = ""
        }

        findViewById<Button>(R.id.btnGoToGame).setOnClickListener {
            startActivity(android.content.Intent(this, GameActivity::class.java))
        }
    }
}
