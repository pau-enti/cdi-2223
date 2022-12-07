package com.example.firstapp.messenger.contacts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstapp.messenger.contacts.model.Contact
import java.io.IOError
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ContactsViewModel : ViewModel() {

    val contacts = MutableLiveData<ArrayList<Contact>>()
    private val FILENAME = "contacts.dat"

    fun saveData(context: Context) {
        val data = contacts.value ?: return

        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            val oos = ObjectOutputStream(it)
            oos.writeObject(data)
        }
    }

    fun loadData(context: Context) {
        try {
            context.openFileInput(FILENAME).use {
                val input = ObjectInputStream(it)
                val data = input.readObject() ?: return@use

                if (data is ArrayList<*>) {
                    contacts.value = data.filterIsInstance<Contact>() as ArrayList
                }
            }
        } catch (_: IOException) {
            // pass
        }

        if (contacts.value == null)
            contacts.value = arrayListOf()
    }

    fun addContact(contact: Contact) {
        contacts.value?.add(contact)

        // notificar observadors
        contacts.value = contacts.value
    }

    fun removeContact(contact: Contact) {
        contacts.value?.remove(contact)

        // notificar observadors
        contacts.value = contacts.value
    }


}