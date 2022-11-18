package com.example.firstapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firstapp.databinding.FragmentSecondRoundBinding


class SecondRoundFragment : Fragment() {

    private lateinit var binding: FragmentSecondRoundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSecondRoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun checkRound(): Boolean {
        return binding.switch1.isChecked &&
                !binding.switch2.isChecked &&
                binding.switch4.isChecked
    }
}