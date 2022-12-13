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
    val db = Firebase.database("https://cdi-2022-default-rtdb.firebaseio.com/")
        .getReference("chats") // collection name

    private val currentUser: String
        get() = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("Not logged in")

    fun openChatWith(user: String) {
        val id = Chat.idChatOf(user, currentUser).toString()
        val request = db.child(id).get()

        request.addOnSuccessListener {
            val cht = it.value

            if (cht != null && cht is Chat) {
                chat.postValue(cht!!)
                subscribe(cht)
            } else {
                createChatWith(user)
            }
        }

        request.addOnFailureListener {
            createChatWith(user)
        }
    }

    private fun createChatWith(user: String) {
        val cht = Chat(user, "Chat with $user")
        db.child(cht.id.toString()).setValue(cht)
        subscribe(cht)
        chat.postValue(cht)
    }

    private fun subscribe(cht: Chat) {
        db.child(cht.id?.toString() ?: return).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typeIndicator = object : GenericTypeIndicator<ArrayList<ChatMessage>>() {}
                val messages = snapshot.child("messages").getValue(typeIndicator)

                if (messages != null) {
                    cht.messages = messages
                    chat.postValue(cht)
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun sendMessage(text: String) {
        val message = ChatMessage(currentUser, text, System.currentTimeMillis())
        val cht = chat.value ?: return

        cht.messages.add(message)
        db.child(cht.id?.toString() ?: return)
            .setValue(cht)
        chat.postValue(cht)
    }

}