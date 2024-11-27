package com.example.myapplication.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.Reto
import com.example.myapplication.model.Pokemon
import com.example.myapplication.webservice.ApiService
import com.example.myapplication.webservice.ApiUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException

class RetosRepository (val context: Context){
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
//    private var retoDao:RetoDao = RetoDB.getDatabase(context).retoDao()
    private var apiService: ApiService = ApiUtils.getApiService()

    suspend fun saveReto(reto: Reto) {
        withContext(Dispatchers.IO) {
            try {

                val documentReference = db.collection("Users").document(uid!!).collection("Retos").add(reto).await()
                val documentId = documentReference.id
                // Actualizar el Reto con el documentId
                reto.documentId = documentId

                db.collection("Users").document(uid).collection("Retos").document(documentId).set(reto).await()
                db.collection("Users").document(uid).collection("Retos").document(documentId).update("createdAt", FieldValue.serverTimestamp()).await()

            } catch (e: Exception) {
                Log.e("FirestoreError", "Error saving reto: ", e)
            }
        }
    }


    suspend fun getRetosList(): List<Pair<String, Reto>> {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = db
                    .collection("Users")
                    .document(uid!!)
                    .collection("Retos")
                    .orderBy("createdAt")
                    .get()
                    .await()
                snapshot.documents.map { document ->
                    val reto = document.toObject(Reto::class.java)
                    val id = document.id // Aquí obtienes el document ID.
                    Pair(id, reto!!)
                }
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al obtener la lista de retos: ", e)
                emptyList()
            }
        }
    }

    suspend fun getRandomReto(): Reto? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = db
                    .collection("Users")
                    .document(uid!!)
                    .collection("Retos")
                    .get()
                    .await()
                val retos = snapshot.documents.mapNotNull { it.toObject(Reto::class.java) }
                retos.randomOrNull()
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error fetching random reto: ", e)
                null
            }
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

    suspend fun getRetoById(documentId: String): Reto? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = db
                    .collection("Users")
                    .document(uid!!)
                    .collection("Retos")
                    .document(documentId)
                    .get()
                    .await()
                snapshot.toObject(Reto::class.java)
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al obtener el reto por ID: ", e)
                null
            }
        }
    }

    suspend fun updateReto(documentId: String, newDescription: String) {
        withContext(Dispatchers.IO) {
            try {
                db
                    .collection("Users")
                    .document(uid!!)
                    .collection("Retos")
                    .document(documentId)
                    .update("description", newDescription)
                    .await()
                Log.d("Firestore", "Reto actualizado con ID: $documentId")
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al actualizar el reto: ", e)
            }
        }
    }

    suspend fun deleteReto(documentId: String) {
        withContext(Dispatchers.IO) {
            try {
                db
                    .collection("Users")
                    .document(uid!!)
                    .collection("Retos")
                    .document(documentId)
                    .delete()
                    .await()
                Log.d("Firestore", "Reto eliminado con ID: $documentId")
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error al eliminar el reto: ", e)
            }
        }
    }


}