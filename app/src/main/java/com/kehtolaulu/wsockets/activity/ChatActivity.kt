package com.kehtolaulu.wsockets.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.kehtolaulu.wsockets.MessageSocket
import com.kehtolaulu.wsockets.R
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val messageSocket: MessageSocket = MessageSocket(
        this,
        { message ->
            runOnUiThread {
                if (message != null) {
                    getMessages()
                }
            }

        },
        { messages ->
            runOnUiThread {
                if (messages != null) {
                    val response: MessageResponse =
                        Gson().fromJson(messages, MessageResponse::class.java)
                    response.items?.let { showMessages(it) }
                }
            }
        })

    private fun showMessages(messages: List<Message>) {
        tv_chat.text = ""
        for (message: Message in messages) {
            tv_chat.text = """
                
                            ${tv_chat.text}
                            ${message.user}: ${message.message}
                            
                            """.trimIndent()
        }
    }

    private fun getMessages() = messageSocket.getMessages(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        messageSocket.initSocketManager()
        btn_send_message.setOnClickListener {
            messageSocket.sendMessage(et_message_text.text.toString())
        }
    }
}

data class Message(val id: Long, val message: String, val user: String)

data class MessageResponse(var items: List<Message>? = null, var first: Int? = null)
