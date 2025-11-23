package com.example.erva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erva.databinding.ActivityChatbotBinding

class ChatbotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private val messageList = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatAdapter = ChatAdapter(messageList)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = chatAdapter

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.editTextMessage.text.clear()
            }
        }
    }

    private fun sendMessage(messageText: String) {
        val userMessage = Message(messageText, true)
        messageList.add(userMessage)
        chatAdapter.notifyItemInserted(messageList.size - 1)

        // Simulate a response from the chatbot
        val botResponse = Message("I am a simple chatbot. How can I help you?", false)
        messageList.add(botResponse)
        chatAdapter.notifyItemInserted(messageList.size - 1)

        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
    }
}
