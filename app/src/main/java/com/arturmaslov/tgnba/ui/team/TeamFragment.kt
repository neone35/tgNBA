package com.arturmaslov.tgnba.ui.team

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturmaslov.tgnba.databinding.FragmentTeamBinding
import com.arturmaslov.tgnba.ui.home.TeamVM

class TeamFragment : Fragment() {

    private lateinit var binding: FragmentTeamBinding
    private lateinit var teamVM: TeamVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamVM = ViewModelProvider(this).get(TeamVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTeamBinding.bind(view)

        val textView: TextView = binding.textTeam
        teamVM.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}