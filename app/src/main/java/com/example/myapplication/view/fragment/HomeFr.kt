package com.example.myapplication.view.fragment
import com.example.myapplication.databinding.FrHomeBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFr : Fragment() {
    private lateinit var binding: FrHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

}