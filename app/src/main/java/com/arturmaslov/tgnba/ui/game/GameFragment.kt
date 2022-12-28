package com.arturmaslov.tgnba.ui.game

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arturmaslov.tgnba.databinding.FragmentGameBinding
import com.arturmaslov.tgnba.ui.home.GameVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val gameVM: GameVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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