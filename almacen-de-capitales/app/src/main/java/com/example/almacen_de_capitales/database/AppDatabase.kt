import android.content.Context
import android.util.Log
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
                Log.d("DB", "Creando nueva instancia de la base de datos")
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