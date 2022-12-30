package com.arturmaslov.tgnba.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Player
import com.arturmaslov.tgnba.databinding.ItemPlayerBinding

class PlayerListAdapter(
    private val playerList: MutableList<Player?>?,
    private val playerVM: PlayerVM
) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {

    private lateinit var itemBinding: ItemPlayerBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerListAdapter.ViewHolder {
        itemBinding = ItemPlayerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PlayerListAdapter.ViewHolder, position: Int) {
        val player = playerList?.get(position)
        if (player != null) {
            holder.bind(player)
        }
    }

    override fun getItemCount(): Int {
        return playerList?.size ?: 0
    }

    // replace orders with new remote list
    fun updatePlayerList(playerList: List<Player?>?) {
        // clear old list
        this.playerList?.clear()
        // create new list and set previous expanded states
        playerList?.forEach { order ->
            if (order != null) {
                this.playerList?.add(order)
            }
        }
    }

    inner class ViewHolder(private val itemBinding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(player: Player) {
            itemBinding.tvPlayerFirstName.text = player.firstName
            itemBinding.tvPlayerLastName.text = player.lastName
            itemBinding.tvPlayerTeam.text = player.team?.name

            itemBinding.clPlayerItemParent.setOnClickListener {
                val navController = it.findNavController()
                if (navController.currentDestination?.id == R.id.frag_game) {
                    player.team?.let { team -> playerVM.setBaseTeam(team) }
                    navController.navigate(R.id.action_frag_player_to_frag_game)
                }
            }
        }
    }

}