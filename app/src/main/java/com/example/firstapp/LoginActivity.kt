package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.firstapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val CORRECT_USERNAME = "pau@enti.cat"
    private val CORRECT_PASSWORD = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (username == CORRECT_USERNAME && password == CORRECT_PASSWORD) {

                binding.loginProgressBar.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.Default).launch {
                    delay(3000)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }

            } else {
                Toast.makeText(this, getString(R.string.error_password), Toast.LENGTH_SHORT).show()
            }
        }

        binding.userInput.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val username = binding.userInput.text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches())
                    binding.userInput.error = "Invalid username"
                else
                    binding.userInput.error = null
            }
        }
    }
}