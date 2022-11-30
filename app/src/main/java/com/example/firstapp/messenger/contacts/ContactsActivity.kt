package com.example.firstapp.messenger.contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityContactsBinding
import com.example.particles.ui.chat.contacts.ContactsRecyclerViewAdapter


class ContactsActivity : AppCompatActivity() {

    lateinit var binding: ActivityContactsBinding
    private lateinit var adapter: ContactsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactsRecyclerViewAdapter(this)
        binding.contactsRecyclerView.adapter = adapter

    }
}