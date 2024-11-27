package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.view.viewholder.OnEditClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RetosListBinding
import com.example.myapplication.model.Reto
import com.example.myapplication.view.viewholder.RetoViewHolder

class RetoAdapter (private val listReto:MutableList<Reto>,private val editClickListener: OnEditClickListener): RecyclerView.Adapter<RetoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = RetosListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RetoViewHolder(binding,editClickListener)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        val reto = listReto[position]
        holder.setRetoList(reto)
    }

    override fun getItemCount(): Int {
        return listReto.size
    }
    fun updateRetos(newList: List<Reto>) {
        listReto.clear()
        listReto.addAll(newList)
        notifyDataSetChanged()
    }
}
