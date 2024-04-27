package com.nestnet.nestapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nestnet.nestapp.R
import com.nestnet.nestapp.models.ChatMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ChatAdapter(private val currentUser: String, private val currentName: String, private var receiverName: String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val messages: MutableList<ChatMessage> = mutableListOf()
    private val senders: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        val previousSender = if (position > 0) senders[position - 1] else null
        holder.bind(message, previousSender)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setMessages(newMessages: List<ChatMessage>) {
        messages.clear()
        senders.clear()
        messages.addAll(newMessages)
        senders.addAll(newMessages.map { it.sender })
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        senders.add(message.sender)
        notifyItemInserted(messages.size - 1)
    }

    fun setReceiverName(name: String) {
        receiverName = name
        notifyDataSetChanged()
    }


    fun formatMessageDate(messageDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val dateTime = LocalDateTime.parse(messageDate, formatter)
        val epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val currentDate = LocalDate.now()
        val messageDateFormatted: String

        messageDateFormatted = when {
            dateTime.toLocalDate() == currentDate -> "Dziś o ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            dateTime.toLocalDate().plusDays(1) == currentDate -> "Wczoraj o ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            else -> dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        }

        return messageDateFormatted
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layoutYourMessage: LinearLayout = itemView.findViewById(R.id.layoutYourMessage)
        private val layoutOtherUserMessage: LinearLayout = itemView.findViewById(R.id.layoutOtherUserMessage)
        private val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewDateOther: TextView = itemView.findViewById(R.id.textViewDateOther)
        private val textViewMessageOtherUser: TextView = itemView.findViewById(R.id.textViewMessageOtherUser)

        var dateview = "false"
        var dateviewother = "false"

        fun bind(message: ChatMessage, previousSender: String?) {
            if (message.sender == currentUser) {
                layoutYourMessage.visibility = View.VISIBLE
                layoutOtherUserMessage.visibility = View.GONE
                textViewDate.text = formatMessageDate(message.date)
                textViewMessage.text = message.message
                textViewDate.setOnClickListener {
                    textViewDate.visibility = if (textViewDate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            } else {
                layoutYourMessage.visibility = View.GONE
                layoutOtherUserMessage.visibility = View.VISIBLE
                textViewDateOther.text = formatMessageDate(message.date)
                textViewMessageOtherUser.text = message.message
                textViewDateOther.setOnClickListener {
                    textViewDateOther.visibility = if (textViewDateOther.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
    }
}