package com.example.almacen_de_capitales.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen_de_capitales.entity.CityEntity
import com.example.almacen_de_capitales.repositoy.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CityRepository): ViewModel() {

    fun insertCity(name: String, country: String, population: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertCity(name, country, population)
        }
    }
}