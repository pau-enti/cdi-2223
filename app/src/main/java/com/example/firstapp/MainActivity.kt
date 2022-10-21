package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firstapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonHelloWorld.setOnClickListener {
            Toast.makeText(this, "Com esteu?", Toast.LENGTH_LONG).show()
        }

        binding.paletteButton.setOnClickListener {
            Snackbar.make(it, "Your message is sent", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    // Code to undo send
                }.show()
        }

    }
}