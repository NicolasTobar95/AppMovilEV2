package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class RegisterActivity : AppCompatActivity() {

    private lateinit var campoNombre: EditText
    private lateinit var campoCorreo: EditText
    private lateinit var campoContrasena: EditText
    private lateinit var campoConfirmar: EditText
    private lateinit var botonRegistrar: Button
    private lateinit var botonVolver: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        campoNombre = findViewById(R.id.editNombre)
        campoCorreo = findViewById(R.id.editCorreo)
        campoContrasena = findViewById(R.id.editContrasenaReg)
        campoConfirmar = findViewById(R.id.editConfirmarContrasena)
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
        val confirmPass = campoConfirmar.text.toString().trim()
        val nombreUsuario = campoNombre.text.toString().trim()

        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || nombreUsuario.isEmpty()) {
            mostrarDialogo("Error", "Completa todos los campos.")
            return
        }

        if (pass != confirmPass) {
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
                    // Enviar datos a firebase usuario y email
                    guardarUsuarioEnFirestore(nombreUsuario, email)

                } else {
                    mostrarDialogo("Error al registrar", task.exception?.message ?: "Error desconocido")
                }
            }
    }
    private fun guardarUsuarioEnFirestore(newUser: String, newEmail: String) {

        val usuario = newUser
        val emailAutor = newEmail

        val nuevoUsuario = hashMapOf(
            "usuarioEmail" to emailAutor,
            "usuarioNombre" to usuario
        )

        db.collection("usuariosapp")
            .add(nuevoUsuario)
            .addOnSuccessListener {
                // Mensaje de éxito y vuelta al Login
                Toast.makeText(this, "Usuario registrado correctamente. Inicie sesión.", Toast.LENGTH_LONG).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Cierra el registro
            }
            .addOnFailureListener { e ->
                mostrarDialogo("Error", "No se pudo guardar la información de usuario: ${e.message}")
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