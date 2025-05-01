package com.example.juego_adivinar_numero_aleatorio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.juego_adivinar_numero_aleatorio.ui.theme.JuegoadivinarnumeroaleatorioTheme
import androidx.compose.foundation.layout.Arrangement

class MainActivity : ComponentActivity() {

    private lateinit var db: GameDbHelper
    private var randomNumber by mutableStateOf(1)
    private var score by mutableStateOf(0)
    private var fails by mutableStateOf(0)
    private var guess by mutableStateOf(TextFieldValue(""))

    private fun generateNewNumber() {
        randomNumber = (1..5).random()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuegoadivinarnumeroaleatorioTheme {

                db = GameDbHelper(this)
                fails = db.getFails()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Intentos fallidos: $fails")
                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = guess,
                                onValueChange = { guess = it },
                                label = { Text("Ingresa un número entre 1 y 5") },
                                modifier = Modifier.padding(16.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val userGuess = guess.text.toIntOrNull()
                                    if (userGuess == null || userGuess !in 1..5) {
                                        fails = db.getFails()
                                    } else {
                                        if (userGuess == randomNumber) {
                                            score += 10
                                            db.resetFails()
                                        } else {
                                            fails += 1
                                            db.setFails(fails)
                                        }

                                        if (fails >= 5) {
                                            score = 0
                                            db.resetFails()
                                        }
                                    }

                                    generateNewNumber()
                                    fails = db.getFails()
                                }
                            ) {
                                Text("Adivinar")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = when {
                                    fails >= 5 -> "Perdiste. Puntaje reiniciado."
                                    randomNumber == (guess.text.toIntOrNull() ?: -1) -> "¡Correcto! +10 puntos"
                                    else -> "Sigue intentando"
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
