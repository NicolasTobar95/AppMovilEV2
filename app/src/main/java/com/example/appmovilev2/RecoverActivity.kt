package com.example.appmovilev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RecoverActivity : AppCompatActivity() {

    // Variables con nombres claros en español
    private lateinit var campoCorreo: EditText
    private lateinit var botonEnviar: Button
    private lateinit var botonVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        // Referencias a los elementos del XML
        campoCorreo = findViewById(R.id.editCorreoRec)
        botonEnviar = findViewById(R.id.btnEnviarRec)
        botonVolver = findViewById(R.id.btnVolverRec)

        // Acción: Enviar correo de recuperación
        botonEnviar.setOnClickListener {
            val correoIngresado = campoCorreo.text.toString().trim()
            if (correoIngresado.isEmpty()) {
                mostrarDialogo("Error", "Por favor, ingrese su correo electrónico.")
            } else {
                mostrarDialogo("Recuperación", "Correo de recuperación enviado correctamente.")
            }
        }

        // Acción: Volver al Login (simplemente cierra esta pantalla)
        botonVolver.setOnClickListener {
            finish()
        }
    }

    // Metodo genérico para mostrar mensajes al usuario
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}