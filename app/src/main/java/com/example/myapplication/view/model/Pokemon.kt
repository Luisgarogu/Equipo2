package com.example.myapplication.view.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name")
    val name: String,

    @SerializedName("img")
    val img: String,
)

data class PokemonResponse(
    @SerializedName("pokemon")
    val pokemon: List<Pokemon>
)