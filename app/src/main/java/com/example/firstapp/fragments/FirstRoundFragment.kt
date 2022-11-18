package com.example.firstapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firstapp.databinding.FragmentFirstRoundBinding

class FirstRoundFragment : Fragment() {

    private lateinit var binding: FragmentFirstRoundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFirstRoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun checkRound(): Boolean {
        return binding.correctRB.isChecked
    }
}