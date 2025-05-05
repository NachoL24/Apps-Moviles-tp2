package com.example.juego_adivinar_numero_aleatorio

import android.content.SharedPreferences
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var db: GameDbHelper
    private var randomNumber: Int = 1
    private lateinit var sharedPref: SharedPreferences
    private var isDarkMode = false

    private fun generateNewNumber() {
        randomNumber = (1..5).random()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = getPreferences(MODE_PRIVATE)
        isDarkMode = sharedPref.getBoolean("isDarkMode", isDarkMode())
        setAppTheme(isDarkMode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = GameDbHelper(this)
        generateNewNumber()

        val iconThemeToggle = findViewById<ImageView>(R.id.iconThemeToggle)
        val textMessage = findViewById<TextView>(R.id.textMessage)
        val inputGuess = findViewById<EditText>(R.id.inputGuess)
        val btnGuess = findViewById<Button>(R.id.btnGuess)
        val textAttempts = findViewById<TextView>(R.id.textAttempts)
        val textPoints = findViewById<TextView>(R.id.textPoints)
        val textBestRunPoints = findViewById<TextView>(R.id.bestRunPoints)
        val dateBestRun = findViewById<TextView>(R.id.dateBestRun)

        var attempt = db.getLastAttempt()
        var bestAttempt = db.getBestAttempt() ?: attempt

        iconThemeToggle.setImageResource(if (isDarkMode) R.drawable.ic_moon else R.drawable.ic_sun)

        updateAttemptUI(attempt, textAttempts, textPoints)
        textBestRunPoints.text = bestAttempt.points.toString()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d/M/yy", Locale.getDefault())
        var parsedDate: Date = inputFormat.parse(bestAttempt.date)
        var formattedDate = outputFormat.format(parsedDate)

        textBestRunPoints.text = bestAttempt.points.toString()
        dateBestRun.text = if (bestAttempt == attempt) "Hoy" else formattedDate

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

            if (userGuess == null || userGuess !in 1..5) {
                Toast.makeText(this, "Por favor ingresa un número válido entre 1 y 5", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userGuess == randomNumber) {
                attempt.points += 10
                attempt.fails = 0
                db.modifyAttempt(attempt)
                textMessage.text = "¡Correcto! +10 puntos"
                updateAttemptUI(attempt, textAttempts, textPoints)

                generateNewNumber()
                inputGuess.text.clear()

            } else {
                attempt.fails += 1
                db.modifyAttempt(attempt)
                textMessage.text = "Incorrecto, sigue intentando"
                updateAttemptUI(attempt, textAttempts, textPoints)
            }

            if (attempt.points > bestAttempt.points) {
                textBestRunPoints.text = attempt.points.toString()
                dateBestRun.text = "Ahora"
            }

            if (attempt.fails >= 5) {
                attempt = db.createAttempt()
                textMessage.text = "¡Perdiste! Intentalo de nuevo"
                updateAttemptUI(attempt, textAttempts, textPoints)
                bestAttempt = db.getBestAttempt() ?: attempt
                textBestRunPoints.text = bestAttempt.points.toString()
                parsedDate = inputFormat.parse(bestAttempt.date)
                formattedDate = outputFormat.format(parsedDate)
                dateBestRun.text = if (bestAttempt == attempt) "Hoy" else formattedDate

                inputGuess.text.clear()
                generateNewNumber()
            }
        }
    }

    private fun updateAttemptUI(attempt: Attempt, textAttempts: TextView, textPoints: TextView) {
        textAttempts.text = attempt.fails.toString()
        textPoints.text = attempt.points.toString()
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        setTheme(if (isDarkMode) R.style.Theme_Dark else R.style.Theme_Light)
    }
}
