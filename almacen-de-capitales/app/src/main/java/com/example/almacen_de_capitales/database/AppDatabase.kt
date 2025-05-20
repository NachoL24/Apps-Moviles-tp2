package com.example.almacen_de_capitales.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.almacen_de_capitales.dao.CityDao
import com.example.almacen_de_capitales.entity.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capital_cities_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}