package com.nestnet.nestapp.models

data class ChatMessage(
    val sender: String,
    val receiver: String,
    val message: String,
    val date: String
)