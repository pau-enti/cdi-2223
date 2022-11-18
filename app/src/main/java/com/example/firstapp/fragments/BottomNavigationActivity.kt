package com.example.firstapp.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityBottomNavigationBinding

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorsRound = FirstRoundFragment()
        val numbersRound = SecondRoundFragment()

        changeFragment(colorsRound)

        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.colorsButton -> changeFragment(colorsRound)
                R.id.numbersButton -> changeFragment(numbersRound)
                R.id.quotesBbutton -> Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach {
                hide(it)
            }

            if (!fragment.isAdded)
                add(binding.fragmentContainer.id, fragment)
            else
                show(fragment)

            commit()
        }
    }
}