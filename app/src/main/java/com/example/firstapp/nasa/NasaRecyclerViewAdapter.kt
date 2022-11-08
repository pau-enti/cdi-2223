package com.example.firstapp.nasa

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemNasaImageBinding
import com.squareup.picasso.Picasso

class NasaRecyclerViewAdapter: RecyclerView.Adapter<NasaRecyclerViewAdapter.NasaVH>() {

    private var photosList: List<NasaImage>? = null

    inner class NasaVH(binding: ItemNasaImageBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.nasaImage
        val title = binding.nasaTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNasaImageBinding.inflate(inflater, parent, false)
        return NasaVH(binding)
    }

    override fun onBindViewHolder(holder: NasaVH, position: Int) {
        val image = photosList?.get(position)
        holder.title.text = image?.title ?: ""
        Picasso.get()
            .load(image?.link)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return photosList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<NasaImage>) {
        photosList = newList
        notifyDataSetChanged()
    }
}