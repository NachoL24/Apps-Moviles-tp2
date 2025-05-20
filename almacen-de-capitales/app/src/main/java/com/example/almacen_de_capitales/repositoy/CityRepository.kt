package com.example.almacen_de_capitales.repository

import com.example.almacen_de_capitales.dao.CityDao
import com.example.almacen_de_capitales.entity.CityEntity
import kotlinx.coroutines.flow.Flow

class CityRepository(private val dao: CityDao) {
    fun allCities(): Flow<List<CityEntity>> = dao.getAllCities()

    fun search(q: String): Flow<List<CityEntity>> =
        if (q.isBlank()) dao.getAllCities() else dao.searchCities(q)

    suspend fun add(city: CityEntity) = dao.insertCity(city)
    suspend fun deleteCity(id: Long) = dao.deleteCityById(id)
    suspend fun deleteCountry(country: String) = dao.deleteCitiesByCountry(country)
    suspend fun updatePop(id: Long, pop: Int) = dao.updatePopulation(id, pop)
}