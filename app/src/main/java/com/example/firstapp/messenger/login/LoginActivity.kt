package com.example.firstapp.messenger.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.firstapp.MainActivity
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityLoginBinding
import com.example.firstapp.messenger.contacts.ContactsActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        val currentUser: String
            get() = FirebaseAuth.getInstance().currentUser?.email ?: "Anonymous"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()

            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnSuccessListener {
                    val intent = Intent(this@LoginActivity, ContactsActivity::class.java)
                    startActivity(intent)

                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, getString(R.string.error_password), Toast.LENGTH_SHORT).show()
                }
        }

        binding.registerButton.setOnClickListener {
            val username = binding.userInput.text.toString()
            val password = binding.passwordInput.text.toString()

            firebaseAuth.createUserWithEmailAndPassword(username, password)
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