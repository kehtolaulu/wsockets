package com.kehtolaulu.wsockets

import android.content.Context
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class SocketListener(
    private val context: Context,
    val onMessageCallback: (String) -> Unit,
    val getMessagesCallback: (String) -> Unit,
    val planBSocket: (WebSocketListener) -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val deviceId =
            context
                .getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE)
                .getString("device_id", "")
        val json = "{ \"device_id\":\"$deviceId\" }"
        webSocket.send(json)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (text == "{\"status\": \"ok\"}") {
            onMessageCallback(text)
        } else if (text.contains("items")) {
            getMessagesCallback(text)
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(0, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        planBSocket(this)
    }

}