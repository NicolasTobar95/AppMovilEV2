package com.example.appmovilev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RecoverActivity : AppCompatActivity() {

    private lateinit var campoCorreo: EditText
    private lateinit var botonEnviar: Button
    private lateinit var botonVolver: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        auth = FirebaseAuth.getInstance()

        campoCorreo = findViewById(R.id.editCorreoRec)
        botonEnviar = findViewById(R.id.btnEnviarRec)
        botonVolver = findViewById(R.id.btnVolverRec)

        botonEnviar.setOnClickListener {
            enviarCorreo()
        }

        botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun enviarCorreo() {
        val email = campoCorreo.text.toString().trim()

        if (email.isEmpty()) {
            mostrarDialogo("Atención", "Por favor ingresa tu correo electrónico.")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mostrarDialogo("Correo Enviado", "Revisa tu bandeja de entrada (y spam) para restablecer tu clave.")
                } else {
                    mostrarDialogo("Error", "No se pudo enviar el correo: ${task.exception?.message}")
                }
            }
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}