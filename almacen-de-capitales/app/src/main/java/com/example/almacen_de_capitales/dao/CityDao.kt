package com.example.almacen_de_capitales.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.almacen_de_capitales.entity.CityEntity

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity):Long

    @Query("SELECT * FROM capital_cities WHERE LOWER(cityName) LIKE LOWER('%' || :name  || '%')")
    suspend fun getCityByName(name: String): List<CityEntity>

    @Query("DELETE FROM capital_cities WHERE id = :id")
    suspend fun deleteCityById(id: Long)

    @Query("DELETE FROM capital_cities WHERE countryName = :country")
    suspend fun deleteCitiesByCountry(country: String)

    @Query("UPDATE capital_cities SET population = :newPopulation WHERE id = :id")
    suspend fun updatePopulation(id: Long, newPopulation: Int)
}