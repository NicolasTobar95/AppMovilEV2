package com.example.appmovilev2

import com.google.firebase.Timestamp

data class Receta(
    var id: String = "",           // ID de Firestore
    val titulo: String = "",
    val descripcion: String = "",  // Resumen corto
    val ingredientes: String = "",
    val preparacion: String = "",  // Pasos a seguir
    val autorEmail: String = "",   // Quien la creo
    val autorNombre: String = "", // Nombre del autor
    val fecha: Timestamp? = null,   // Fecha de creacion
    val categoria: String = "General", // Guarda el nombre de la imagen (ej: "Pizza")
    val url: String = ""
)

// Función para obtener la imagen según el texto de la categoría
fun obtenerImagenPorCategoria(categoria: String): Int {
    return when (categoria) {
        "Salteado" -> R.drawable.img_salteado_de_huevo_con_tomate
        "Olla Caliente" -> R.drawable.img_olla_caliente
        "Pasta" -> R.drawable.img_pasta
        "Sopa" -> R.drawable.img_sopa_caliente
        "Pizza" -> R.drawable.img_pizza
        "Churrasco" -> R.drawable.img_churrasco
        "Ramen" -> R.drawable.img_ramen
        "Hamburguesa" -> R.drawable.img_hamburguesa
        "Dieta" -> R.drawable.img_dieta
        else -> R.drawable.img_default // Imagen por defecto
    }
}