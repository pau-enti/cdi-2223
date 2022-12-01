package com.example.firstapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.firstapp.databinding.TestLayoutBinding

class TestActivity : AppCompatActivity() {

    lateinit var binding: TestLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = TestLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listener = View.OnClickListener {
            val button = it as Button

            when (it.id) {
                R.id.button2 -> "a"
            }
        }

        binding.button2.setOnClickListener(listener)
        binding.button3.setOnClickListener(listener)
        binding.button4.setOnClickListener(listener)
        binding.button2.setOnClickListener(listener)

        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val signature = shared.getString("username", null)

        val editor = shared.edit()
        editor.putInt("dni", 89238293)
        editor.apply()

    }
}