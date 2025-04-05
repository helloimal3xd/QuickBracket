package com.proyecto.quickbracket.ui.brackets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BracketView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        textSize = 40f
    }

    var bracketData: List<BracketNode> = listOf() // Lista con los enfrentamientos

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBracket(canvas, bracketData)
    }

    private fun drawBracket(canvas: Canvas, nodes: List<BracketNode>) {
        var startX = 100f
        var startY = 200f
        val stepY = 150f // Espacio entre rondas

        nodes.forEach { node ->
            // Dibujar los equipos
            canvas.drawText(node.equipo1, startX, startY, paint)
            canvas.drawText(node.equipo2, startX, startY + 50, paint)

            // Dibujar línea entre enfrentamientos
            canvas.drawLine(startX + 100, startY + 25, startX + 200, startY + 25, paint)

            // Avanzar a la siguiente posición
            startY += stepY
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Aquí detectamos qué enfrentamiento ha sido tocado
            val touchedNode = detectarNodoTocado(event.x, event.y)
            if (touchedNode != null) {
                // Mostrar un diálogo para elegir ganador
                mostrarSeleccionGanador(touchedNode)
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun detectarNodoTocado(x: Float, y: Float): BracketNode? {
        // Aquí puedes implementar lógica para detectar qué enfrentamiento ha sido tocado
        return bracketData.firstOrNull() // Temporal, se debe mejorar
    }

    private fun mostrarSeleccionGanador(nodo: BracketNode) {
        // Mostrar un diálogo con los nombres de los equipos para elegir el ganador
        // Al seleccionar, actualizar nodo.ganador y redibujar la vista
    }
}
