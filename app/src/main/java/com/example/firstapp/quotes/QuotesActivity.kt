package com.example.firstapp.quotes

import android.animation.ObjectAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.animation.doOnEnd
import com.example.firstapp.databinding.ActivityQuotesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nextQuote()
        nextRandomColor()

        binding.nextQuoteButton.setOnClickListener {
            nextQuote()
        }
    }

    fun nextQuote() {
        val outside = Retrofit.Builder()
            .baseUrl("http://quotes.stormconsultancy.co.uk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val services = outside.create(ApiQuotes::class.java)
        services.getQuote().enqueue(object : Callback<Quote> {
            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                val quote = response.body()
                binding.quoteTextView.text = quote?.text ?: "Something went wrong :("
                binding.authorTextView.text = quote?.author ?: ""
            }

            override fun onFailure(call: Call<Quote>, t: Throwable) {
                binding.quoteTextView.text = "Something went wrong :("
                binding.authorTextView.text = ""
            }
        })
    }

    private var lastColor = 0

    fun nextRandomColor() {
        val newColor: Int = Color.argb(150, (0..256).random(), (0..256).random(), (0..256).random())

        ObjectAnimator.ofArgb(
            binding.quotesLayout, "backgroundColor",
            lastColor,
            newColor
        ).apply {
            duration = 2000

            start()
        }.doOnEnd {
            nextRandomColor()
        }

        lastColor = newColor
    }
}