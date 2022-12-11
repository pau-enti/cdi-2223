package com.example.firstapp.messenger.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstapp.messenger.chat.model.Chat
import com.example.firstapp.messenger.chat.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel() {
    val chat = MutableLiveData<Chat>()

    private val db = Firebase.database("https://cdi-2022-default-rtdb.firebaseio.com/")
        .getReference("chats")

    private val currentUser: String
        get() = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("Not logged in")

    fun openChatWith(user: String) {
        val id = Chat.idChatOf(currentUser, user).toString()
        val request = db.child(id).get()

        request.addOnSuccessListener {
            val data = it.value

            if (data == null || data !is Chat)
                return@addOnSuccessListener createChatWith(user)

            chat.postValue(data!!)
            subscribe(data)
        }

    }

    private fun subscribe(cht: Chat) {
        db.child(cht.id.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<ArrayList<ChatMessage>>() {}
                val messages = snapshot.child("messages").getValue(typeIndicator)

                if (messages != null) {
                    cht.messages = messages
                } // else { some error happened }

                chat.postValue(cht)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    private fun createChatWith(user: String) {
        val newChat = Chat(currentUser, user)
        db.child(newChat.id.toString()).setValue(newChat)
        chat.postValue(newChat)
        subscribe(newChat)
    }

    fun sendMessage(text: String) {
        val cht = chat.value ?: return
        cht.addMessage(text)
        db.child(cht.id.toString()).setValue(cht)
    }


}