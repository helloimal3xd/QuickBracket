package com.proyecto.quickbracket.ui.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//esta clase se encarga de convertir la lista de strings en un solo string
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromListString(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
