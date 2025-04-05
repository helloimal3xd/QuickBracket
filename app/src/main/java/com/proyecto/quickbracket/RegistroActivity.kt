package com.proyecto.quickbracket

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.quickbracket.databinding.ActivityRegisterBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val constraintLayout = binding.mainLayout
        val animationDrawable = constraintLayout.getBackground() as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        auth = FirebaseAuth.getInstance()

        binding.btnRegistro.setOnClickListener {
            val email = binding.etCorreo.text.toString()
            val password = binding.etContrasena.text.toString()
            val password2 = binding.etContrasena2.text.toString()

            if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(this, "completa todos los campos", Toast.LENGTH_SHORT).show()
            } else if (password != password2) {
                Toast.makeText(
                    this,
                    "las contraseñas no coinciden, revisa nuevamente",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registrarUsuario(email, password)
            }

        }
    }

    private fun registrarUsuario(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }

            }

    }
}
