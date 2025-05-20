package com.example.almacen_de_capitales.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.almacen_de_capitales.repositoy.CityRepository

class CityViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            return CityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
