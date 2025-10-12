package com.example.appmovilev2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {

    // Variables para los campos de texto y botones
    private lateinit var campoUsuario: EditText
    private lateinit var campoContrasena: EditText
    private lateinit var botonIniciar: Button
    private lateinit var botonRegistrar: Button
    private lateinit var botonRecuperar: Button
    private lateinit var botonBluetooth: Button

    // Código de solicitud del permiso Bluetooth
    private val CODIGO_SOLICITUD_BLUETOOTH = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Asignación de vistas (referencias del XML)
        campoUsuario = findViewById(R.id.editUsuario)
        campoContrasena = findViewById(R.id.editContrasena)
        botonIniciar = findViewById(R.id.btnIniciar)
        botonRegistrar = findViewById(R.id.btnRegistrar)
        botonRecuperar = findViewById(R.id.btnRecuperar)
        botonBluetooth = findViewById(R.id.btnBluetooth)

        // Acción: Iniciar sesión
        botonIniciar.setOnClickListener {
            val usuarioTexto = campoUsuario.text.toString().trim()
            val contrasenaTexto = campoContrasena.text.toString().trim()

            if (usuarioTexto.isEmpty() || contrasenaTexto.isEmpty()) {
                mostrarDialogo("Error", "Por favor ingrese usuario y contraseña.")
            } else {
                mostrarDialogo("Inicio de sesión", "Inicio de sesión exitoso.")
            }
        }

        // Acción: Ir a Registrar cuenta
        botonRegistrar.setOnClickListener {
            val intentRegistro = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegistro)
        }

        // Acción: Ir a Recuperar clave
        botonRecuperar.setOnClickListener {
            val intentRecuperar = Intent(this@LoginActivity, RecoverActivity::class.java)
            startActivity(intentRecuperar)
        }

        // Acción: Ver estado del Bluetooth
        botonBluetooth.setOnClickListener {
            verificarPermisoBluetooth()
        }
    }

    // Metodo para mostrar un cuadro de diálogo (alerta simple)
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    // Verifica si el permiso de Bluetooth está otorgado o lo solicita
    private fun verificarPermisoBluetooth() {
        val permisoBluetooth = Manifest.permission.BLUETOOTH_CONNECT
        when {
            ContextCompat.checkSelfPermission(this, permisoBluetooth) == PackageManager.PERMISSION_GRANTED -> {
                mostrarEstadoBluetooth()
            }
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(permisoBluetooth), CODIGO_SOLICITUD_BLUETOOTH)
            }
        }
    }

    // Muestra el estado actual del Bluetooth (activado/desactivado)
    private fun mostrarEstadoBluetooth() {
        val adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter()
        if (adaptadorBluetooth == null) {
            mostrarDialogo("Bluetooth", "Este dispositivo no soporta Bluetooth.")
            return
        }

        val estadoBluetooth = if (adaptadorBluetooth.isEnabled)
            "Bluetooth activado"
        else
            "Bluetooth desactivado"

        mostrarDialogo("Estado del Bluetooth", estadoBluetooth)
    }

    // Maneja la respuesta del usuario al permiso solicitado
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permisos: Array<out String>,
        resultados: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permisos, resultados)
        if (requestCode == CODIGO_SOLICITUD_BLUETOOTH) {
            if (resultados.isNotEmpty() && resultados[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarEstadoBluetooth()
            } else {
                mostrarDialogo("Permiso denegado", "No se puede acceder al estado del Bluetooth sin permiso.")
            }
        }
    }
}