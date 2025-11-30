package com.example.appmovilev2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class VerRecetaActivity : AppCompatActivity() {

    private lateinit var imgDetalle: ImageView
    private lateinit var tvTitulo: TextView
    private lateinit var tvAutorFecha: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvIngredientes: TextView
    private lateinit var tvPreparacion: TextView
    private lateinit var btnVolver: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_receta)

        db = FirebaseFirestore.getInstance()

        // Vincular vistas
        imgDetalle = findViewById(R.id.imgDetalleGrande)
        tvTitulo = findViewById(R.id.txtTituloDetalle)
        tvAutorFecha = findViewById(R.id.txtAutorFecha)
        tvDescripcion = findViewById(R.id.txtDescripcionDetalle)
        tvIngredientes = findViewById(R.id.txtIngredientesDetalle)
        tvPreparacion = findViewById(R.id.txtPreparacionDetalle)
        btnVolver = findViewById(R.id.btnVolverDetalle)

        val idReceta = intent.getStringExtra("RECETA_ID")

        if (idReceta != null) {
            cargarDetalleReceta(idReceta)
        } else {
            Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarDetalleReceta(id: String) {
        db.collection("recetas").document(id)
            .get()
            .addOnSuccessListener { documento ->
                if (documento.exists()) {
                    val receta = documento.toObject(Receta::class.java)

                    if (receta != null) {
                        // Cargar textos
                        tvTitulo.text = receta.titulo
                        tvDescripcion.text = receta.descripcion
                        tvIngredientes.text = receta.ingredientes
                        tvPreparacion.text = receta.preparacion

                        // Cargar Imagen según categoría usando la función de Receta.kt
                        val imagenId = obtenerImagenPorCategoria(receta.categoria)
                        imgDetalle.setImageResource(imagenId)

                        // Fecha
                        val fechaFormateada = if (receta.fecha != null) {
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            sdf.format(receta.fecha.toDate())
                        } else {
                            "--/--/----"
                        }
                        val nombreMostrar = if (receta.autorNombre.isNotEmpty()) receta.autorNombre else "Anónimo"
                        tvAutorFecha.text = "Por: ${nombreMostrar} | Fecha: $fechaFormateada"
                    }
                } else {
                    Toast.makeText(this, "Receta no encontrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
    }
}