package com.example.firstapp.messenger.chat.model

import com.example.firstapp.messenger.login.LoginActivity


class Chat private constructor(
    val id: Int? = null,
    private val user1: String? = null,
    private val user2: String? = null,
    var name: String? = null,
    var messages: ArrayList<ChatMessage> = arrayListOf()
) {
    constructor(
        user: String,
        name: String? = null
    ) : this(idChatOf(myself, user), myself, user, name)

    val interlocutor: String
        get() = (if (myself == user1) user2 else user1)
            ?: throw IllegalStateException("Bad data from db")

    companion object {
        val myself: String
            get() = LoginActivity.currentUser

        fun idChatOf(user1: String, user2: String): Int = setOf(user1, user2).hashCode()
    }

    fun addMessage(m: String) {
        messages.add(ChatMessage(myself, m, System.currentTimeMillis()))
    }
}
