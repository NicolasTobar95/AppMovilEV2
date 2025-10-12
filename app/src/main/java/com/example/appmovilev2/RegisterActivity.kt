package com.example.appmovilev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    // Campos de entrada y botones
    private lateinit var campoNombre: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoContrasena: EditText
    private lateinit var botonRegistrar: Button
    private lateinit var botonVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Vinculación de vistas
        campoNombre = findViewById(R.id.editNombre)
        campoCorreo = findViewById(R.id.editCorreo)
        campoContrasena = findViewById(R.id.editContrasenaReg)
        botonRegistrar = findViewById(R.id.btnRegistrarCuenta)
        botonVolver = findViewById(R.id.btnVolverLogin)

        // Acción: Registrar cuenta
        botonRegistrar.setOnClickListener {
            val nombre = campoNombre.text.toString().trim()
            val correo = campoCorreo.text.toString().trim()
            val contrasena = campoContrasena.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                mostrarDialogo("Error", "Por favor, complete todos los campos.")
            } else {
                mostrarDialogo("Registro exitoso", "La cuenta ha sido registrada correctamente.")
            }
        }

        // Acción: Volver al login (simplemente cierra esta actividad)
        botonVolver.setOnClickListener {
            finish()
        }
    }

    // Metodo común para mostrar mensajes
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
