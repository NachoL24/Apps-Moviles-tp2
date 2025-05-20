package com.example.almacen_de_capitales

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.almacen_de_capitales.databinding.ActivityCreateCityBinding
import com.example.almacen_de_capitales.entity.CityEntity
import com.example.almacen_de_capitales.viewmodel.CityViewModel
import com.google.android.material.snackbar.Snackbar

class CreateCityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCityBinding
    private val vm: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Botón Guardar */
        binding.save.setOnClickListener {
            val name=binding.inputCityName.text.toString().trim()
            val country=binding.inputCountryName.text.toString().trim()
            val popText=binding.inputPopulation.text.toString().trim()
            val pop=popText.toIntOrNull()

            /* Validación */
            if (name.isBlank() || country.isBlank() || pop == null) {
                Snackbar.make(binding.root, "Completa todos los campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.add(
                CityEntity(
                    cityName=name,
                    countryName=country,
                    population=pop
                )
            )
            finish()
        }
    }
}
