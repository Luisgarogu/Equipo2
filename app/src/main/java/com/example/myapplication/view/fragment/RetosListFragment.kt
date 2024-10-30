package com.example.myapplication.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.widget.Button

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentRetosListBinding
import com.example.myapplication.view.adapter.RetoAdapter
import com.example.myapplication.view.dialog.AddRetoDialog
import com.example.myapplication.view.viewholder.OnEditClickListener
import com.example.myapplication.viewmodel.RetosViewModel

class RetosListFragment : Fragment(), OnEditClickListener {

    private lateinit var binding: FragmentRetosListBinding
    private val retosViewModel: RetosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRetosListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.contentToolbar.toolbar
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Retos"
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        val recycler = binding.recyclerview
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recycler.layoutManager = layoutManager
        val adapter = RetoAdapter(retosViewModel.retosList.value ?: mutableListOf(), this)
        recycler.adapter = adapter

        observerRetosList()
    }

    private fun setupAddButton() {
        val dialog = AddRetoDialog(retosViewModel) {
            observerRetosList()
        }
        binding.fbagregar.setOnClickListener {
            dialog.showDialog(binding.root.context)
        }
    }

    private fun observerRetosList() {
        retosViewModel.getRetosList()
        retosViewModel.retosList.observe(viewLifecycleOwner) { retosList ->
            val adapter = binding.recyclerview.adapter as RetoAdapter
            adapter.updateRetos(retosList)
        }
    }


    override fun onEditClick(retoId: Int) {
        // Aquí deberías abrir un AlertDialog o hacer alguna otra acción cuando se haga clic en Editar.
        // Puedes utilizar el ID del reto (retoId) para realizar operaciones adicionales.
        println("Edit Clicked for Reto ID: $retoId")
        showEditDialog(retoId)
    }

    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(retoId: Int) {
        val viewModel = retosViewModel // Reemplaza con tu referencia real al ViewModel

        // Inflate del layout personalizado
        val inflater = LayoutInflater.from(requireContext())
        val customLayout = inflater.inflate(R.layout.dialog_edit, null)

        // Encuentra vistas en el layout personalizado
        val tvDialogTitle = customLayout.findViewById<TextView>(R.id.tvDialogTitle)
        val etDescription = customLayout.findViewById<EditText>(R.id.etDescription)
        val btnSave = customLayout.findViewById<Button>(R.id.btnSave)
        val btnCancel = customLayout.findViewById<Button>(R.id.btnCancel)

        // Configuración del AlertDialog
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setView(customLayout)

        // Configuración del título

        // Obtener el reto por ID
        val currentReto = viewModel.getRetoById(retoId)

        // Establecer la descripción actual en el EditText
        etDescription.setText(currentReto?.description)

        // Configurar clics en botones
        btnSave.setOnClickListener {
            // Manejar clic en botón Guardar
            val newDescription = etDescription.text.toString()
            currentReto?.let {
                viewModel.updateRetoDescription(it.id, newDescription)
            }
            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            // Manejar clic en botón Cancelar
            alertDialog.dismiss()
        }

        // Evitar que el diálogo se cierre al tocar fuera de él
        alertDialog.setCanceledOnTouchOutside(false)

        // Mostrar el AlertDialog
        alertDialog.show()
    }



    override fun onDeleteClick(retoId: Int) {
        // Lógica para manejar el clic en el botón de eliminar
        // Puedes mostrar un diálogo de confirmación para la eliminación aquí.
        showDeleteConfirmationDialog(retoId)
    }

    private fun showDeleteConfirmationDialog(retoId: Int) {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_confirmation, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()

        val btnConfirmDelete = dialogView.findViewById<Button>(R.id.btnConfirmDelete)
        val btnCancelDelete = dialogView.findViewById<Button>(R.id.btnCancelDelete)
        val tvRetoDescription = dialogView.findViewById<TextView>(R.id.tvRetoDescription)

        // Obtener el reto por ID
        val currentReto = retosViewModel.getRetoById(retoId)

        // Mostrar la descripción del reto en el TextView del diálogo
        tvRetoDescription.text = currentReto?.description ?: "No description available"

        btnConfirmDelete.setOnClickListener {
            // Lógica para eliminar el desafío
            retosViewModel.deleteReto(retoId)
            alertDialog.dismiss()
        }

        btnCancelDelete.setOnClickListener {
            alertDialog.dismiss()
        }

        // Evitar que el diálogo se cierre al tocar fuera de él
        alertDialog.setCanceledOnTouchOutside(false)

        alertDialog.show()
    }




}