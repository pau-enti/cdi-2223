package com.example.firstapp.nasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.example.firstapp.databinding.ActivityNasaBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNasaBinding
    private val adapter = NasaRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nasaRecyclerView.adapter = adapter

        binding.nasaSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                preformSearch(query ?: return true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun preformSearch(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://images-api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ApiNasa::class.java)
        call.preformSearch(query).enqueue(object : Callback<ImagesList> {
            override fun onResponse(call: Call<ImagesList>, response: Response<ImagesList>) {
                val images = response.body() ?: return
                val nasaImages = images.collection.items.map {
                    val data = it.data?.getOrNull(0)
                    val link = it.links?.getOrNull(0)?.href ?: ""
                    NasaImage(data?.title ?: "", data?.description ?: "", link)
                }
                adapter.submitList(nasaImages)
            }

            override fun onFailure(call: Call<ImagesList>, t: Throwable) {
                Toast.makeText(this@NasaActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }
}