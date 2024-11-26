package com.example.myapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FrRulesBinding
import com.example.myapplication.R

class RulesFr : Fragment() {
    private lateinit var binding: FrRulesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrRulesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.contentToolbar.toolbar
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "Reglas del Juego"

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}