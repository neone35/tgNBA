package com.arturmaslov.tgnba.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Player
import com.arturmaslov.tgnba.databinding.ItemPlayerBinding
import com.arturmaslov.tgnba.utils.Constants

class PlayerListAdapter(
    private val playerList: MutableList<Player?>?
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

    fun updatePlayerList(playerList: List<Player?>?) {
        this.playerList?.clear()
        playerList?.forEach { player ->
            if (player != null) {
                this.playerList?.add(player)
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
                if (navController.currentDestination?.id == R.id.frag_player) {
                    val bundle = bundleOf(
                        Constants.TEAM_ID to player.team?.id,
                        Constants.TEAM_NAME to player.team?.name,
                    )
                    navController.navigate(R.id.action_frag_player_to_frag_game, bundle)
                }
            }
        }
    }

}