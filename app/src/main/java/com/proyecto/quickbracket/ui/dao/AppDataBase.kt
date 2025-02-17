package com.proyecto.quickbracket.ui.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proyecto.quickbracket.ui.dao.TorneoDao

@Database(entities = [TorneoEntity::class], version = 1,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun torneoDao(): TorneoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "torneo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}