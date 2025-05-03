package com.example.juego_adivinar_numero_aleatorio

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: GameDbHelper
    private var randomNumber: Int = 1
    private lateinit var sharedPref: SharedPreferences
    private var isDarkMode = false

    private fun generateNewNumber() {
        randomNumber = (1..5).random()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getPreferences(MODE_PRIVATE)
        isDarkMode = sharedPref.getBoolean("isDarkMode", isDarkMode())

        setAppTheme(isDarkMode)

        setContentView(R.layout.activity_main)

        db = GameDbHelper(this)
        db.resetFails()
        generateNewNumber()

        val iconThemeToggle = findViewById<ImageView>(R.id.iconThemeToggle)
        val textMessage = findViewById<TextView>(R.id.textMessage)
        val inputGuess = findViewById<EditText>(R.id.inputGuess)
        val btnGuess = findViewById<Button>(R.id.btnGuess)
        val textAttempts = findViewById<TextView>(R.id.textAttempts)

        iconThemeToggle.setImageResource(if (isDarkMode) R.drawable.ic_moon else R.drawable.ic_sun)

        textAttempts.text = "Intentos fallidos: ${db.getFails()}"

        iconThemeToggle.setOnClickListener {
            isDarkMode = !isDarkMode
            with(sharedPref.edit()) {
                putBoolean("isDarkMode", isDarkMode)
                apply()
            }
            recreate()
        }

        btnGuess.setOnClickListener {
            val userGuess = inputGuess.text.toString().toIntOrNull()
            val fails = db.getFails()

            if (userGuess == null || userGuess !in 1..5) {
                Toast.makeText(this, "Por favor ingresa un número válido entre 1 y 5", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userGuess == randomNumber) {
                Toast.makeText(this, "¡Correcto! +10 puntos", Toast.LENGTH_SHORT).show()
                db.resetFails()
                textMessage.text = "¡Correcto!"
                generateNewNumber()
            } else {
                db.setFails(fails + 1)
                Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                textMessage.text = "Sigue intentando"
            }

            val updatedFails = db.getFails()
            textAttempts.text = "Intentos fallidos: $updatedFails"

            if (updatedFails >= 5) {
                db.resetFails()
                Toast.makeText(this, "¡Perdiste! Tu puntuación se reinicia.", Toast.LENGTH_SHORT).show()
                textMessage.text = "¡Perdiste!"
                textAttempts.text = "Intentos fallidos: 0"
                generateNewNumber()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.resetFails()
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        setTheme(if (isDarkMode) R.style.Theme_Dark else R.style.Theme_Light)
    }
}
