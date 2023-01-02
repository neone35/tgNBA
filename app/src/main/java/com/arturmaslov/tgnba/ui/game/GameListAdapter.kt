package com.arturmaslov.tgnba.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.data.models.Game
import com.arturmaslov.tgnba.databinding.ItemGameBinding

class GameListAdapter(
    private var gameList: MutableList<Game?>?
) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    private lateinit var itemBinding: ItemGameBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameListAdapter.ViewHolder {
        itemBinding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GameListAdapter.ViewHolder, position: Int) {
        val game = gameList?.get(position)
        if (game != null) {
            holder.bind(game)
        }
    }

    override fun getItemCount(): Int {
        return gameList?.size ?: 0
    }

    fun replaceGameList(gameList: List<Game?>?) {
        this.gameList = gameList?.sortedByDescending { it?.date }?.toMutableList()
    }

    inner class ViewHolder(private val itemBinding: ItemGameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(game: Game) {
            itemBinding.tvGameHomeTeam.text = game.homeTeam?.name
            itemBinding.tvGameHomeScore.text = game.homeTeamScore.toString()
            itemBinding.tvGameVisitorTeam.text = game.visitorTeam?.name
            itemBinding.tvGameVisitorScore.text = game.visitorTeamScore.toString()
        }
    }

}