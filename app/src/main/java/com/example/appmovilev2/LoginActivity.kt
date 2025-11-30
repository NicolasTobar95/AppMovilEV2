package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    // Variables de UI
    private lateinit var campoUsuario: EditText
    private lateinit var campoContrasena: EditText
    private lateinit var botonIniciar: Button
    private lateinit var botonGoogle: Button
    private lateinit var botonRegistrar: TextView
    private lateinit var botonRecuperar: TextView

    // Variables de Firebase
    private lateinit var auth: FirebaseAuth
    private val CODIGO_GOOGLE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 2. Verificar si ya hay alguien logueado (Auto-login)
        if (auth.currentUser != null) {
            irAlHome()
        }

        // 3. Vincular vistas
        campoUsuario = findViewById(R.id.editUsuario)
        campoContrasena = findViewById(R.id.editContrasena)
        botonIniciar = findViewById(R.id.btnIniciar)
        botonGoogle = findViewById(R.id.btnGoogle)
        botonRegistrar = findViewById(R.id.btnRegistrar)
        botonRecuperar = findViewById(R.id.btnRecuperar)

        // 4. Configurar botones
        botonIniciar.setOnClickListener {
            loginConEmail()
        }

        botonGoogle.setOnClickListener {
            loginConGoogle()
        }

        botonRegistrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        botonRecuperar.setOnClickListener {
            startActivity(Intent(this, RecoverActivity::class.java))
        }
    }

    private fun loginConEmail() {
        val email = campoUsuario.text.toString().trim()
        val pass = campoContrasena.text.toString().trim()

        if (email.isEmpty() || pass.isEmpty()) {
            mostrarDialogo("Error", "Por favor ingrese correo y contraseña")
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login exitoso -> Usamos Toast porque es una notificación rápida y no invasiva
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    irAlHome()
                } else {
                    mostrarDialogo("Error de Acceso", task.exception?.message ?: "Error desconocido")
                }
            }
    }

    private fun loginConGoogle() {
        // Configuración para pedir el email y el token
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Firebase genera esto automático
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, gso)
        // Lanzamos la ventana de Google
        startActivityForResult(googleClient.signInIntent, CODIGO_GOOGLE)
    }

    // Recibimos el resultado de la ventana de Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODIGO_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google dijo que sí, ahora avisamos a Firebase
                val cuenta = task.getResult(ApiException::class.java)
                autenticarEnFirebase(cuenta.idToken!!)
            } catch (e: ApiException) {
                mostrarDialogo("Error Google", "Fallo: ${e.message}")
            }
        }
    }

    private fun autenticarEnFirebase(token: String) {
        val credencial = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credencial)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    irAlHome()
                } else {
                    mostrarDialogo("Error", "No se pudo autenticar en Firebase")
                }
            }
    }

    private fun irAlHome() {
        val intent = Intent(this, HomeActivity::class.java)
        // Estas flags evitan que el usuario vuelva al login pulsando "Atrás"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}