package com.example.particles.ui.chat.contacts

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemContactBinding
import com.example.firstapp.messenger.contacts.model.Contact

class ContactsRecyclerViewAdapter(val activity: Activity) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {

    private var contacts: List<Contact> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name


    }

    override fun getItemCount(): Int = contacts.size

    inner class ViewHolder(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.contactName
        val image: ImageView = binding.contactImage
    }

}