package com.example.myapplication.view.viewholder

import android.view.MotionEvent
import com.example.myapplication.view.viewholder.OnEditClickListener

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RetosListBinding
import com.example.myapplication.model.Reto
interface OnEditClickListener {
    fun onEditClick(retoId: Int)
    fun onDeleteClick(retoId: Int)
}

class RetoViewHolder (binding: RetosListBinding,private val editClickListener: OnEditClickListener):RecyclerView.ViewHolder(binding.root) {
    val bindingReto = binding

    fun setRetoList(reto: Reto){
        bindingReto.tvDescription.text = reto.description



        bindingReto.ivEdit.setOnClickListener {
            // Llamar al método onEditClick del listener
            animateTouch(bindingReto.ivEdit)
            editClickListener.onEditClick(reto.id)
        }
        bindingReto.ivDelete.setOnClickListener {
            // Llamar al método onDeleteClick del listener
            animateTouch(bindingReto.ivDelete)
            editClickListener.onDeleteClick(reto.id)
        }
    }

    private fun animateTouch(imageView: ImageView) {
        // Escala hacia abajo en el eje X e Y cuando se presiona
        imageView.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
            // Restablece la escala original después de la animación
            imageView.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }
}

