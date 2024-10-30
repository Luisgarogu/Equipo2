package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reto(
    @PrimaryKey (autoGenerate = true)
    val id:Int = 0,
    var description:String
)