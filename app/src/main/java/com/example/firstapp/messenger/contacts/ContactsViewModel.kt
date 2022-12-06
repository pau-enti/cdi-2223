package com.example.firstapp.messenger.contacts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstapp.messenger.contacts.model.Contact
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ContactsViewModel : ViewModel() {

    val contacts = MutableLiveData<ArrayList<Contact>>()
    private val DATA_FILENAME = "chat_contacts.dat"

    fun saveData(context: Context) {
        val data = contacts.value ?: return
        context.openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE).use {
            val oos = ObjectOutputStream(it)
            oos.writeObject(data)
        }
    }


    fun loadData(context: Context) {
        try {
            context.openFileInput(DATA_FILENAME).use {
                val iin = ObjectInputStream(it)
                val data = iin.readObject() ?: return@use

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
        val list = contacts.value ?: return
        list.add(contact)
        contacts.value = list
    }

    fun deleteContact(contact: Contact) {
        val list = contacts.value ?: return
        list.remove(contact)
        contacts.value = list
    }


}