package com.example.myapplication.view.webservice

import com.example.myapplication.view.model.PokemonResponse
import com.example.myapplication.utils.Constants.POKEMON_ENDPOINT
import retrofit2.http.GET

interface ApiService {
    @GET(POKEMON_ENDPOINT)
    suspend fun getPokemonList(): PokemonResponse

}