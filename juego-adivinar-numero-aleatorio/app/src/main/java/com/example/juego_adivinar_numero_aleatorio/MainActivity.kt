package com.example.juego_adivinar_numero_aleatorio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.rotate
import com.example.juego_adivinar_numero_aleatorio.ui.theme.JuegoadivinarnumeroaleatorioTheme

class MainActivity : ComponentActivity() {

    private lateinit var db: GameDbHelper
    private var randomNumber by mutableStateOf(1)

    private fun generateNewNumber() {
        randomNumber = (1..5).random()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = GameDbHelper(this)
        generateNewNumber()

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var score by remember { mutableStateOf(0) }
            var fails by remember { mutableStateOf(db.getFails()) }
            var guess by remember { mutableStateOf(TextFieldValue("")) }

            JuegoadivinarnumeroaleatorioTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            ThemeToggleButton(
                                isDarkTheme = isDarkTheme,
                                onToggle = { isDarkTheme = !isDarkTheme }
                            )
                        }
                    },
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

                            Button(onClick = {
                                val userGuess = guess.text.toIntOrNull()
                                if (userGuess == null || userGuess !in 1..5) {
                                    fails = db.getFails()
                                } else {
                                    if (userGuess == randomNumber) {
                                        score += 10
                                        db.resetFails()
                                        fails = 0
                                    } else {
                                        fails += 1
                                        db.setFails(fails)
                                    }

                                    if (fails >= 5) {
                                        score = 0
                                        db.resetFails()
                                        fails = 0
                                    }
                                }

                                generateNewNumber()
                                fails = db.getFails()
                            }) {
                                Text("Adivinar")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = when {
                                    fails >= 5 -> "¡Perdiste! Ahora tu puntuación es 0." // Mensaje al perder
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

@Composable
fun ThemeToggleButton(isDarkTheme: Boolean, onToggle: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 180f else 0f,
        label = "icon rotation"
    )

    IconButton(onClick = onToggle) {
        Icon(
            painter = painterResource(
                if (isDarkTheme) R.drawable.ic_moon else R.drawable.ic_sun
            ),
            contentDescription = "Cambiar tema",
            modifier = Modifier
                .size(32.dp)
                .rotate(rotation),
            tint = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface
        )
    }
}
