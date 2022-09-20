package com.example.cs496_pj2_ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cs496_pj2_ui.databinding.ChatMainFragmentBinding

class ChatMainFragment : Fragment() {

    private lateinit var binding: ChatMainFragmentBinding
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        id = arguments?.getString("id")!!
        binding = ChatMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Fetching Chatting Data

    }
}