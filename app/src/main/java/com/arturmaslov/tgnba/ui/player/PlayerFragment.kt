package com.arturmaslov.tgnba.ui.player

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturmaslov.tgnba.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var playerVM: PlayerVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerVM = ViewModelProvider(this).get(PlayerVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)

        val textView: TextView = binding.textPlayer
        playerVM.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

    }

}