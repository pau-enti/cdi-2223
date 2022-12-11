package com.example.firstapp.messenger.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.R
import com.example.firstapp.databinding.ItemReceivedMessageBinding
import com.example.firstapp.databinding.ItemSentMessageBinding
import com.example.firstapp.messenger.chat.model.Chat
import com.example.firstapp.messenger.chat.model.ChatMessage
import com.example.firstapp.messenger.login.LoginActivity


@SuppressLint("NotifyDataSetChanged")
class ChatRecyclerViewAdapter :
    RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatVH>() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var messages: ArrayList<ChatMessage> = ArrayList()

    fun notifyNewMessage(chat: Chat) {
        messages = chat.messages
        notifyDataSetChanged()
        layoutManager?.scrollToPosition(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = if (viewType == SENT_TYPE)
            layoutInflater.inflate(R.layout.item_sent_message, parent, false)
        else
            layoutInflater.inflate(R.layout.item_received_message, parent, false)

        return ChatVH(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        // 0 -> sent message
        // 1 -> received message
        val author = LoginActivity.currentUser
        return if (messages[position].author == author) SENT_TYPE else RECEIVED_TYPE
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.message.text = messages[position].content
    }

    override fun getItemCount(): Int = messages.size

    inner class ChatVH(view: View, type: Int) : RecyclerView.ViewHolder(view) {
        val message: TextView = if (type == SENT_TYPE)
            ItemSentMessageBinding.bind(view).myMessage
        else
            ItemReceivedMessageBinding.bind(view).othersMessage
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // L'objecte layoutManager està a la vista XML, hem d'esperar a que s'enganxi a la vista
        // per tal d'obtenir-lo.
        layoutManager = recyclerView.layoutManager
        super.onAttachedToRecyclerView(recyclerView)
    }

    companion object {
        const val SENT_TYPE = 0
        const val RECEIVED_TYPE = 1
    }
}