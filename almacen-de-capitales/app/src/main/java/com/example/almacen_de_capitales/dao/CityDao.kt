package com.example.almacen_de_capitales.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.almacen_de_capitales.entity.CityEntity

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("SELECT * FROM capital_cities WHERE cityName = :name")
    suspend fun getCityByName(name: String): CityEntity?

    @Query("DELETE FROM capital_cities WHERE cityName = :name")
    suspend fun deleteCityByName(name: String)

    @Query("DELETE FROM capital_cities WHERE countryName = :country")
    suspend fun deleteCitiesByCountry(country: String)

    @Query("UPDATE capital_cities SET population = :newPopulation WHERE cityName = :cityName")
    suspend fun updatePopulation(cityName: String, newPopulation: Int)
}