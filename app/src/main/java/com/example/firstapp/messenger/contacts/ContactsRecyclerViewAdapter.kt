package com.example.firstapp.messenger.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemContactBinding
import com.example.firstapp.messenger.chat.ChatActivity
import com.example.firstapp.messenger.contacts.model.Contact

class ContactsRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(ChatActivity.EXTRA_USER_ID, contact.userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.contactName
        val image: ImageView = binding.contactImage
    }

}