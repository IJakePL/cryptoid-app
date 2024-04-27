package com.nestnet.nestapp.utils

import com.nestnet.nestapp.models.ChatMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class ChatApiService {
    interface Service {
        @GET("/messages/to/{userId}&{userFrom}")
        suspend fun getReceivedMessages(@Path("userId") userId: String, @Path("userFrom") userFrom: String): List<ChatMessage>

        @GET("/messages/from/{userId}&{userFrom}")
        suspend fun getSentMessages(@Path("userId") userId: String, @Path("userFrom") userFrom: String): List<ChatMessage>

        @POST("/messages")
        suspend fun sendMessage(@Body message: ChatMessage)

        @GET("/messages/all/{userId}&{userFrom}")
        suspend fun getAllMessages(@Path("userId") userId: String, @Path("userFrom") userFrom: String): List<ChatMessage>
    }
}
