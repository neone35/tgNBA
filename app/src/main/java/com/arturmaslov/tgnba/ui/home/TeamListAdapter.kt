package com.arturmaslov.tgnba.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.databinding.ItemTeamBinding

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
        val order = teamList?.get(position)
        if (order != null) {
            holder.bind(order)
        }
    }

    override fun getItemCount(): Int {
        return teamList?.size ?: 0
    }

    // replace orders with new remote list
    fun updateOrderList(orderList: List<Team?>?) {
        // clear old list
        teamList?.clear()
        // create new list and set previous expanded states
        orderList?.forEach { order ->
            if (order != null) {
                teamList?.add(order)
            }
        }
    }

    inner class ViewHolder(private val itemBinding: ItemTeamBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(team: Team) {
            itemBinding.tvTeamName.text = team.name
            itemBinding.tvTeamCity.text = team.city
            itemBinding.tvTeamConf.text = team.conference
        }
    }

}