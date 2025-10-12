package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    // Tiempo de espera (2 segundos) antes de pasar a la pantalla de inicio de sesión
    private val tiempoDeEspera = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Referencia al logo del splash (imagen centrada)
        val imagenLogo: ImageView = findViewById(R.id.splashLogo)

        // Handler con retardo: espera y luego abre la pantalla de Login
        Handler(Looper.getMainLooper()).postDelayed({
            val intentLogin = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intentLogin)
            finish() // Cierra el Splash para no volver atrás con el botón "Atrás"
        }, tiempoDeEspera)
    }
}