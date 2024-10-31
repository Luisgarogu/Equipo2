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
            editClickListener.onEditClick(reto.id)
        }
        bindingReto.ivDelete.setOnClickListener {
            // Llamar al método onDeleteClick del listener
            editClickListener.onDeleteClick(reto.id)
        }

    }

    private fun setTouchAnimation(imageView: ImageView) {
        imageView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Escala hacia abajo en el eje X e Y cuando se presiona
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Restablece la escala original cuando se libera o se cancela
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    true
                }
                else -> false
            }
        }
    }



}