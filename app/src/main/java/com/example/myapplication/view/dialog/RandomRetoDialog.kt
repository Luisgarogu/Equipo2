package com.example.myapplication.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.RandomChallengePopupWindowBinding
import com.example.myapplication.model.Pokemon
import com.example.myapplication.viewmodel.RetosViewModel
import kotlin.random.Random
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import android.graphics.drawable.Drawable
import android.util.Log

class RandomRetoDialog (private val challengesViewModel: RetosViewModel) {
    fun showDialog(
        context: Context
    ): AlertDialog {
        val inflater = LayoutInflater.from(context)
        val binding = RandomChallengePopupWindowBinding.inflate(inflater)

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCancelable(false) //para cuando se cliquee por los lados no se pueda
        alertDialog.setView(binding.root) //establecer la vista de un cuadro de dialogo

        // Elijo un pokemon al azar de la lista de pokemones
        val pokemonSize = challengesViewModel.pokemonList.value?.size
        val randomPosition = Random.nextInt(pokemonSize ?: 151)
        val pokemon: Pokemon? = challengesViewModel.pokemonList.value?.get(randomPosition)

        val originalUrl = pokemon?.img
        val secureUrl = originalUrl?.replace("http://", "https://")
        Glide.with(binding.root.context).load(secureUrl).into(binding.PokemonImage)


        // Establezco el challenge en el texto del dialog
        binding.ChallengeText.text = challengesViewModel.randomReto.value?.description.toString()

        binding.CloseBtn.setOnClickListener {
            //Toast.makeText(context,"Aceptar", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        alertDialog.show()

        return alertDialog

    }
}