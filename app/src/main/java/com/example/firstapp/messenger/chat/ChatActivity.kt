package com.example.firstapp.messenger.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityChatBinding
import com.example.firstapp.messenger.chat.model.Chat

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatRecyclerViewAdapter

    private lateinit var chat: Chat

    companion object {
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ChatRecyclerViewAdapter()
        binding.chatView.adapter = adapter

        val interlocutor = intent.extras?.getString(EXTRA_USER_ID) ?: return
        supportActionBar?.title = interlocutor

        chat = Chat(interlocutor)

        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString()
            if (text.isNotBlank()) {
                binding.messageInput.text.clear()

                chat.addMessage(text)
                adapter.notifyNewMessage(chat)
            }
        }
    }
}