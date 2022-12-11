package com.example.firstapp.messenger.chat.model


data class ChatMessage(
    val author: String? = null,
    val content: String? = null,
    val time: Long? = null
)