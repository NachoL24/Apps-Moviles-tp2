package com.example.almacen_de_capitales.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "capital_cities")
data class CityEntity(
    @PrimaryKey val id: Long,
    val cityName: String,
    val countryName: String,
    val population: Int
)