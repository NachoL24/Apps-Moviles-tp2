package com.example.almacen_de_capitales

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.almacen_de_capitales.databinding.CreateCityBinding
import com.google.android.material.textfield.TextInputLayout

class CreateCity: AppCompatActivity() {
    private lateinit var binding: CreateCityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputCityName.addTextChangedListener(createTextWatcher(binding.cityNameLayout))
        binding.inputCountryName.addTextChangedListener(createTextWatcher(binding.countryNameLayout))
        binding.inputPopulation.addTextChangedListener(createTextWatcher(binding.populationLayout))

        binding.save.setOnClickListener {
            val cityName: String = binding.inputCityName.text.toString()
            val countryName: String = binding.inputCountryName.text.toString()
            val populationString: String = binding.inputPopulation.text.toString()

            if (cityName.isBlank() || countryName.isBlank() || populationString.isBlank()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                if (cityName.isBlank()) binding.cityNameLayout.error = "Campo requerido" else binding.cityNameLayout.error = null
                if (countryName.isBlank()) binding.countryNameLayout.error = "Campo requerido" else binding.countryNameLayout.error = null
                if (populationString.isBlank()) binding.populationLayout.error = "Campo requerido" else binding.populationLayout.error = null
                return@setOnClickListener
            } else {
                binding.cityNameLayout.error = null
                binding.countryNameLayout.error = null
                binding.populationLayout.error = null
            }

            val population: Int? = populationString.toIntOrNull()
            if (population == null) {
                Toast.makeText(this, "La población debe ser un número válido", Toast.LENGTH_SHORT).show()
                binding.populationLayout.error = "Número inválido"
                return@setOnClickListener
            } else {
                binding.populationLayout.error = null
            }

            Toast.makeText(this, "Procesando datos...", Toast.LENGTH_LONG).show()
        }
    }

    private fun createTextWatcher(inputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    inputLayout.error = null
                    inputLayout.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }
}