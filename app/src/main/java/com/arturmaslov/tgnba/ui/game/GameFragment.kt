package com.arturmaslov.tgnba.ui.game

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturmaslov.tgnba.databinding.FragmentGameBinding
import com.arturmaslov.tgnba.ui.home.GameVM

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var gameVM: GameVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameVM = ViewModelProvider(this).get(GameVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)

        val textView: TextView = binding.textTeam
        gameVM.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}