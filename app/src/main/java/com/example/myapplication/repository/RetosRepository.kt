package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.data.RetoDB
import com.example.myapplication.data.RetoDao
import com.example.myapplication.model.Reto
import com.example.myapplication.model.Pokemon
import com.example.myapplication.webservice.ApiService
import com.example.myapplication.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RetosRepository (val context: Context){
    private var retoDao:RetoDao = RetoDB.getDatabase(context).retoDao()
    private var apiService: ApiService = ApiUtils.getApiService()
    suspend fun saveReto(reto: Reto){
        withContext(Dispatchers.IO){
            retoDao.saveReto(reto)
        }
    }
    suspend fun getRetosList():MutableList<Reto>{
        return withContext(Dispatchers.IO){
            retoDao.getRetosList()
        }
    }

    suspend fun getRandomReto(): Flow<Reto?> {
        return withContext(Dispatchers.IO){
            retoDao.getRandomReto()
        }
    }

    suspend fun getPokemonList(): List<Pokemon> {
        return withContext(Dispatchers.IO){
            try {
                val response = apiService.getPokemonList()
                response.pokemon.map{ Pokemon(it.name, it.img) }
            } catch (e: Exception){
                // Manejar el error
                e.printStackTrace()
                mutableListOf()
            }
        }
    }
    suspend fun getRetoById(retoId: Int): Reto? {
        return withContext(Dispatchers.IO) {
            retoDao.getRetoById(retoId)
        }
    }

    suspend fun updateReto(reto: Reto) {
        withContext(Dispatchers.IO) {
           retoDao.updateReto(reto)
        }
    }

    suspend fun deleteReto(retoId: Int) {
        withContext(Dispatchers.IO) {
           retoDao.deleteReto(retoId)
        }
    }


}