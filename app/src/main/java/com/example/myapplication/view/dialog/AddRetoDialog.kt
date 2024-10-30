package com.example.myapplication.view.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.example.myapplication.databinding.AddRetoDialogBinding
import com.example.myapplication.model.Reto
import com.example.myapplication.viewmodel.RetosViewModel

class AddRetoDialog (private val retosViewModel: RetosViewModel, private val onClose: () -> Unit) {
    fun showDialog(context: Context) {
        val inflater = LayoutInflater.from(context)
        val binding = AddRetoDialogBinding.inflate(inflater)
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setCancelable(false)
        alertDialog.setView(binding.root)

        binding.btnSave.isEnabled = false
        updateButtonColor(binding.btnSave.isEnabled, binding.btnSave)

        binding.etInsertReto.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnSave.isEnabled = s.toString().isNotEmpty()
                updateButtonColor(binding.btnSave.isEnabled, binding.btnSave)
            }

        })

        binding.btnCancel.setOnClickListener {
            Toast.makeText(context,"Se ha cancelado la accion", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        binding.btnSave.setOnClickListener{
            println("BUTTON CLICKEEEEEDDD!!!")
            saveReto(binding.etInsertReto.text.toString())
            Toast.makeText(context, "Reto adicionado", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
            onClose.invoke()
        }

        alertDialog.show()

    }
    private fun saveReto(description: String){
        println("SAVING CHALLENGEEEEE!!!")
        val reto = Reto(description = description)
        retosViewModel.saveReto(reto)
    }

    private fun updateButtonColor(isEnabled: Boolean, btnSave: MaterialButton) {
        // Cambia el color del botón btnSave según su estado de habilitación
        if (isEnabled) {
            btnSave.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF3A06")) // Color naranja
        } else {
            btnSave.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#808080")) // Color gris
        }
    }
}