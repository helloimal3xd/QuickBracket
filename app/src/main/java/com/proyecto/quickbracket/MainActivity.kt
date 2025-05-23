package com.proyecto.quickbracket

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.proyecto.quickbracket.databinding.ActivityMainBinding
import com.proyecto.quickbracket.ui.ThemeUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeResId(ThemeUtils.getSavedTheme(this)))
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)?.findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_trofeo, R.id.navigation_reglas
            )
        )
        if (navController != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }
        if (navController != null) {
            navView.setupWithNavController(navController)
        }
    }

    private fun getThemeResId(themeName: String): Int {
        return when (themeName) {
            ThemeUtils.THEME_ROJO -> R.style.Theme_QuickBracket_Rojo
            ThemeUtils.THEME_VERDE -> R.style.Theme_QuickBracket_Verde
            else -> R.style.Theme_QuickBracket_azul
        }
    }

}