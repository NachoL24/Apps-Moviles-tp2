package com.example.almacen_de_capitales.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen_de_capitales.entity.CityEntity
import com.example.almacen_de_capitales.repository.CityRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CityRepository(AppDatabase.getInstance(application).cityDao())

    private val _query = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val cities: StateFlow<List<CityEntity>> =
        _query.flatMapLatest { repo.search(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(q: String) { _query.value = q }

    fun add(city: CityEntity) = viewModelScope.launch { repo.add(city) }
    fun deleteCity(id: Long) = viewModelScope.launch { repo.deleteCity(id) }
    fun deleteCountry(country: String) = viewModelScope.launch { repo.deleteCountry(country) }
    fun updatePop(id: Long, pop: Int) = viewModelScope.launch { repo.updatePop(id, pop) }
    fun insertCity(name: String, country: String, population: Int) {
        add(CityEntity(cityName = name, countryName = country, population = population))
    }

}
