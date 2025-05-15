package com.proyecto.quickbracket.ui

import android.content.Context
import com.proyecto.quickbracket.R

object ThemeUtils {
    private const val PREFS_NAME = "theme_prefs"
    private const val THEME_KEY = "app_theme"

    const val THEME_AZUL = "Theme.QuickBracket.azul"
    const val THEME_ROJO = "Theme.QuickBracket.Rojo"
    const val THEME_VERDE = "Theme.QuickBracket.Verde"

    private val themeCycle = listOf(THEME_AZUL, THEME_ROJO, THEME_VERDE)

    fun saveTheme(context: Context, theme: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(THEME_KEY, theme).apply()
    }

    fun getSavedTheme(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(THEME_KEY, THEME_AZUL) ?: THEME_AZUL
    }

    fun getNextTheme(currentTheme: String): String {
        val index = themeCycle.indexOf(currentTheme)
        return themeCycle[(index + 1) % themeCycle.size]
    }

}
