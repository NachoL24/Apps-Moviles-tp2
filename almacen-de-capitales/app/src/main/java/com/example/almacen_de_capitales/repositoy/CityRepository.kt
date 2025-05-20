package com.example.almacen_de_capitales.repositoy

import com.example.almacen_de_capitales.dao.CityDao
import com.example.almacen_de_capitales.entity.CityEntity

class CityRepository(private val cityDao: CityDao) {

    suspend fun insertCity(name: String, country: String, population: Int): CityEntity {


        return CityEntity(
            0,
            "asd",
            "asdasd",
            123
        )
    }
}