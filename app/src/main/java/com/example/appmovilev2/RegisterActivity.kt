package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var campoCorreo: EditText
    private lateinit var campoContrasena: EditText
    private lateinit var campoConfirmar: EditText // NUEVO
    private lateinit var botonRegistrar: Button
    private lateinit var botonVolver: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        campoCorreo = findViewById(R.id.editCorreo) // Asegúrate que coincida con tu XML
        campoContrasena = findViewById(R.id.editContrasenaReg)
        campoConfirmar = findViewById(R.id.editConfirmarContrasena) // NUEVO
        botonRegistrar = findViewById(R.id.btnRegistrarCuenta)
        botonVolver = findViewById(R.id.btnVolverLogin)

        botonRegistrar.setOnClickListener {
            registrarUsuario()
        }

        botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {
        val email = campoCorreo.text.toString().trim()
        val pass = campoContrasena.text.toString().trim()
        val confirmPass = campoConfirmar.text.toString().trim() // NUEVO

        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            mostrarDialogo("Error", "Completa todos los campos.")
            return
        }

        if (pass != confirmPass) { // NUEVA VALIDACIÓN
            mostrarDialogo("Error", "Las contraseñas no coinciden.")
            return
        }

        if (pass.length < 6) {
            mostrarDialogo("Error", "La contraseña debe tener al menos 6 caracteres.")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // CAMBIO: Mensaje de éxito y vuelta al Login
                    Toast.makeText(this, "Usuario registrado correctamente. Inicie sesión.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra el registro
                } else {
                    mostrarDialogo("Error al registrar", task.exception?.message ?: "Error desconocido")
                }
            }
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }
}