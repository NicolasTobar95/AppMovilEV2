package com.example.appmovilev2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AgregarRecetaActivity : AppCompatActivity() {

    // Vistas
    private lateinit var editTitulo: EditText
    private lateinit var editURL: EditText
    private lateinit var editDescripcion: EditText
    private lateinit var editIngredientes: EditText
    private lateinit var editPreparacion: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var imgPreview: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    // Firebase
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Opciones de categoría
    private val opciones = arrayOf(
        "General", "Salteado", "Olla Caliente", "Pasta",
        "Sopa", "Pizza", "Churrasco", "Ramen", "Hamburguesa", "Dieta"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_receta)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Vincular vistas
        editTitulo = findViewById(R.id.editTitulo)
        editURL = findViewById(R.id.editURL)
        editDescripcion = findViewById(R.id.editDescripcion)
        editIngredientes = findViewById(R.id.editIngredientes)
        editPreparacion = findViewById(R.id.editPreparacion)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        imgPreview = findViewById(R.id.imgPrevisualizacion) // <--- VINCULAMOS LA IMAGEN
        btnGuardar = findViewById(R.id.btnGuardarReceta)
        btnCancelar = findViewById(R.id.btnCancelar)

        configurarSpinner()

        btnGuardar.setOnClickListener {
            guardarEnFirestore()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun configurarSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Escuchar cambios en el Spinner
        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 1. Obtener qué texto eligió el usuario (ej: "Pizza")
                val categoriaSeleccionada = opciones[position]

                // 2. Buscar la imagen correspondiente usando nuestra función de Receta.kt
                val imagenId = obtenerImagenPorCategoria(categoriaSeleccionada)

                // 3. Actualizar el ImageView
                imgPreview.setImageResource(imagenId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacemos nada
            }
        }
    }

    private fun guardarEnFirestore() {

        val titulo = editTitulo.text.toString().trim()
        val url = editURL.text.toString().trim()
        val descripcion = editDescripcion.text.toString().trim()
        val ingredientes = editIngredientes.text.toString().trim()
        val preparacion = editPreparacion.text.toString().trim()
        val categoriaSeleccionada = spinnerCategoria.selectedItem.toString()

        if (titulo.isEmpty() || descripcion.isEmpty() || ingredientes.isEmpty() || preparacion.isEmpty()) {
            mostrarAlerta("Faltan datos", "Por favor completa todos los campos.")
            return
        }

        val usuario = auth.currentUser
        val emailAutor = usuario?.email ?: "Anónimo"

        val nombreAutor = if (usuario?.displayName != null && usuario.displayName!!.isNotEmpty()) {
            usuario.displayName!! // Nombre real de Google
        } else {
            emailAutor.substringBefore("@") // "juan" de "juan@gmail.com"
        }

        val nuevaReceta = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "ingredientes" to ingredientes,
            "preparacion" to preparacion,
            "autorEmail" to emailAutor,
            "autorNombre" to nombreAutor,
            "fecha" to Timestamp(Date()),
            "categoria" to categoriaSeleccionada,
            "url" to url
        )

        btnGuardar.isEnabled = false
        btnGuardar.text = "Guardando..."

        db.collection("recetas")
            .add(nuevaReceta)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Receta guardada!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                btnGuardar.isEnabled = true
                btnGuardar.text = "Publicar Receta"
                mostrarAlerta("Error", "No se pudo guardar: ${e.message}")
            }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }
}