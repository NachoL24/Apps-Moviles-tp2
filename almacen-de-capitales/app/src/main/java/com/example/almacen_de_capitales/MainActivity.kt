package com.example.almacen_de_capitales

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.almacen_de_capitales.databinding.MainActivityBinding
import com.example.almacen_de_capitales.entity.CityEntity
import com.example.almacen_de_capitales.viewmodel.CityViewModel
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    private val vm by viewModels<CityViewModel>()
    private lateinit var adapter: CityAdapter
    private lateinit var filterAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater).also { setContentView(it.root) }

        adapter = CityAdapter(
            onInfo = { showInfo(it) },
            onEdit = { editPopulation(it) },
            onDelete = { vm.deleteCity(it.id) }
        )
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                vm.setQuery(q ?: ""); return true
            }
        })

        filterAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.countryFilter.setAdapter(filterAdapter)
        binding.countryFilter.setOnItemClickListener { _, _, pos, _ ->
            filterAdapter.getItem(pos)?.let { confirmDeleteCountry(it) }
        }

        binding.btnAddCity.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateCityActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launchWhenStarted {
            try {
                vm.cities.collect { list: List<CityEntity> ->
                    Log.d("MainActivity", "Lista recibida con ${list.size} ciudades.")
                    adapter.submitList(list)
                    Log.d("MainActivity", "submitList llamado.")
                    updateCountryDropdown(list)
                    Log.d("MainActivity", "updateCountryDropdown llamado.")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error en el collector de vm.cities", e)
                // Aquí podrías mostrar un mensaje al usuario o tomar otra acción
            }
        }
    }

    private fun updateCountryDropdown(list: List<CityEntity>) {
        val countries = list.mapNotNull { it.countryName?.takeIf { name -> name.isNotBlank() } }
                            .distinct()
                            .sorted()
        filterAdapter.clear()
        filterAdapter.addAll(countries) // Si countries es List<String>, esto está bien.
    }

    private fun showInfo(c: CityEntity) = AlertDialog.Builder(this)
        .setTitle("${c.cityName} (${c.countryName})")
        .setMessage("Población: ${c.population}")
        .setPositiveButton("OK", null).show()

    private fun editPopulation(c: CityEntity) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(c.population.toString())
        }
        AlertDialog.Builder(this)
            .setTitle("Actualizar población")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                input.text.toString().toIntOrNull()?.let { vm.updatePop(c.id, it) }
            }
            .setNegativeButton("Cancelar", null).show()
    }

    private fun confirmDeleteCountry(country: String) = AlertDialog.Builder(this)
        .setTitle("Eliminar todas las ciudades de $country?")
        .setPositiveButton("Eliminar") { _, _ -> vm.deleteCountry(country) }
        .setNegativeButton("Cancelar", null).show()

}