package com.example.appmovilev2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var btnAgregar: FloatingActionButton
    private lateinit var btnCerrar: ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Lista local para el adaptador
    private val listaRecetas = mutableListOf<Receta>()
    private lateinit var adapter: RecetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recycler = findViewById(R.id.recyclerRecetas)
        btnAgregar = findViewById(R.id.btnAgregarReceta)
        btnCerrar = findViewById(R.id.btnCerrarSesion)

        configurarLista()
        cargarDatos()

        // Botón Agregar
        btnAgregar.setOnClickListener {
            // Verificar si el usuario está logueado antes de dejarle agregar
            if (auth.currentUser != null) {
                startActivity(Intent(this, AgregarRecetaActivity::class.java))
            } else {
                mostrarAlerta("Atención", "Debes iniciar sesión para agregar recetas.")
            }
        }

        // Botón Cerrar Sesión
        btnCerrar.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun configurarLista() {
        adapter = RecetaAdapter(listaRecetas) { recetaSeleccionada ->
            // Al hacer clic en una receta, vamos a VerRecetaActivity
            val intent = Intent(this, VerRecetaActivity::class.java)
            intent.putExtra("RECETA_ID", recetaSeleccionada.id) // Pasamos el ID
            startActivity(intent)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun cargarDatos() {
        // Escuchamos la colección "recetas" en tiempo real
        db.collection("recetas")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    mostrarAlerta("Error", "No se pudieron cargar las recetas.")
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    listaRecetas.clear()
                    for (documento in snapshots) {
                        // Convertimos el documento a objeto Receta
                        val receta = documento.toObject(Receta::class.java)
                        receta.id = documento.id // Guardamos el ID del documento
                        listaRecetas.add(receta)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun cerrarSesion() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Cierra el Home
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }
}