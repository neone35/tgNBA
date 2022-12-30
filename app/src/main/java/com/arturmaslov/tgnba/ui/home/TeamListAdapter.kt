package com.arturmaslov.tgnba.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.databinding.ItemTeamBinding
import com.arturmaslov.tgnba.utils.Constants

class TeamListAdapter(
    private val teamList: MutableList<Team?>?
) : RecyclerView.Adapter<TeamListAdapter.ViewHolder>() {

    private lateinit var itemBinding: ItemTeamBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamListAdapter.ViewHolder {
        itemBinding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TeamListAdapter.ViewHolder, position: Int) {
        val team = teamList?.get(position)
        if (team != null) {
            holder.bind(team)
        }
    }

    override fun getItemCount(): Int {
        return teamList?.size ?: 0
    }


    fun updateTeamList(teamList: List<Team?>?) {
        this.teamList?.clear()
        teamList?.forEach { team ->
            if (team != null) {
                this.teamList?.add(team)
            }
        }
    }

    inner class ViewHolder(private val itemBinding: ItemTeamBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(team: Team) {
            itemBinding.tvTeamName.text = team.name
            itemBinding.tvTeamCity.text = team.city
            itemBinding.tvTeamConf.text = team.conference

            itemBinding.clTeamItemParent.setOnClickListener {
                val navController = it.findNavController()
                if (navController.currentDestination?.id == R.id.frag_home) {
                    val bundle = bundleOf(
                        Constants.TEAM_ID to team.id,
                        Constants.TEAM_NAME to team.name,
                    )
                    navController.navigate(R.id.action_frag_home_to_frag_game, bundle)
                }
            }
        }
    }

}