package com.example.myapplication.model

import com.google.firebase.Timestamp

data class Reto(
    var description: String = " ",
    var documentId: String = " ",  // Agregado para almacenar el documentId
    var createdAt: Timestamp? = null // Campo para almacenar la fecha y hora de creación
)