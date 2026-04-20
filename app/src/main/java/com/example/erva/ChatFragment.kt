package com.example.erva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.erva.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val messageList = mutableListOf<Message>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter(messageList)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
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
        val botResponse = Message("I am ERVA Chatbot. How can I help you with your plants today?", false)
        messageList.add(botResponse)
        chatAdapter.notifyItemInserted(messageList.size - 1)

        binding.recyclerViewChat.scrollToPosition(messageList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
