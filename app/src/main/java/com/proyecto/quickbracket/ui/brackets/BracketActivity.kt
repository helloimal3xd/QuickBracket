package com.proyecto.quickbracket.ui.brackets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.quickbracket.R

class BracketActivity: AppCompatActivity() {

    private lateinit var bracketView: BracketView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bracket)

        bracketView = findViewById(R.id.bracketView)

        val equipos = intent.getStringArrayListExtra("equipos") ?: listOf()
        bracketView.bracketData = generarBracket(equipos)

    }

    private fun generarBracket(equipos: List<String>): List<BracketNode> {
        val enfrentamientos = mutableListOf<BracketNode>()

        for (i in equipos.indices step 2){
            if (i + 1 < equipos.size){
                enfrentamientos.add(BracketNode(equipos[i], equipos[i + 1]))
            }
        }
        return enfrentamientos
    }
}