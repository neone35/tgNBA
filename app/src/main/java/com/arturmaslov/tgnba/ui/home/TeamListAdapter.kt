package com.arturmaslov.tgnba.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.databinding.ItemTeamBinding

class TeamListAdapter(
    private val teamList: MutableList<Team?>?,
    private val homeVM: HomeVM
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

    // replace orders with new remote list
    fun updateOrderList(teamList: List<Team?>?) {
        // clear old list
        this.teamList?.clear()
        // create new list and set previous expanded states
        teamList?.forEach { order ->
            if (order != null) {
                this.teamList?.add(order)
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
                    homeVM.setBaseTeam(team)
                    navController.navigate(R.id.action_frag_home_to_frag_game)
                }
            }
        }
    }

}