package com.kehtolaulu.wsockets

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import okhttp3.*

class MessageSocket(
    private val context: Context,
    private var messageCallback: (String?) -> Unit,
    private var getMessagesCallback: (String?) -> Unit
) {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private lateinit var socket: WebSocket

    // TODO: Refactor
    private val gson: Gson = Gson()

    fun initSocketManager() {
        val request: Request = Request.Builder()
            .url("wss://backend-chat.cloud.technokratos.com/chat")
            .build()
        val listener = SocketListener(context, messageCallback, getMessagesCallback, {
            socket = client.newWebSocket(request, it)
        })
        socket = client.newWebSocket(request, listener)
    }

    fun getMessages(count: Int) {
        val request = mapOf("history" to mapOf("limit" to count))
        val json = gson.toJson(request)
        socket.send(json)
    }

    fun sendMessage(message: String) {
        val request = mapOf("message" to message)
        val json = gson.toJson(request)
        socket.send(json)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
