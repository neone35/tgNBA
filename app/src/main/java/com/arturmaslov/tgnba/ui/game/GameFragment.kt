package com.arturmaslov.tgnba.ui.game

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Game
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.databinding.FragmentGameBinding
import com.arturmaslov.tgnba.ui.UiHelper
import com.arturmaslov.tgnba.ui.home.GameVM
import com.arturmaslov.tgnba.ui.main.MainActivity
import com.arturmaslov.tgnba.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment(R.layout.fragment_game), UiHelper {

    private lateinit var binding: FragmentGameBinding
    private val gameVM: GameVM by viewModel()
    private var rv: RecyclerView? = null
    private var rvState: Parcelable? = null
    private lateinit var mainActivity: MainActivity

    private var teamId: Int? = null
    private var teamName: String? = null

    private var currentPage: Int = 1
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var firstVisibleItemPos: Int = 0
    private var canLoadMore: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (requireActivity() as MainActivity)
        teamId = arguments?.getInt(Constants.TEAM_ID)
        teamName = arguments?.getString(Constants.TEAM_NAME)
        gameVM.fetchGameList(listOf(teamId), currentPage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        binding.textTeam.text = teamName
        rv = binding.rvTeamList
        setListeners()
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        mainActivity.controlBottomNav(Constants.SLIDE_DOWN)
        mainActivity.controlBottomNav(Constants.HIDE)
    }

    override fun setListeners() {
        binding.textBackHome.setOnClickListener {
            val navController = it.findNavController()
            if (navController.currentDestination?.id == R.id.frag_game) {
                navController.navigate(R.id.action_frag_game_to_frag_home)
            }
        }
    }

    override fun setObservers() {
        mainActivity.observeLoadStatus(gameVM.extLoadStatus)
        mainActivity.observeRepositoryResponse(gameVM.remoteResponse)
        observeLoadStatusForLoadMore(gameVM.extLoadStatus)

        gameVM.extGameList.observe(viewLifecycleOwner) {
            setGameList(it)
        }
    }

    private fun setGameList(gameList: List<Game?>?) {
        val mutablePlayerList = gameList?.toMutableList()
        val currentItemCount = rv?.adapter?.itemCount
        if (currentItemCount == null || currentItemCount == 0) {
            // create new adapter with initial data
            val adapter = GameListAdapter(mutablePlayerList)
            rv?.adapter = adapter
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.scrollToPosition(mainActivity.gameRVPosition)
        } else {
            // update existing adapter with updated data
            rvState = rv?.layoutManager?.onSaveInstanceState()
            val currentAdapter = rv?.adapter as? GameListAdapter
            currentAdapter?.addToGameList(gameList)
            currentAdapter?.notifyItemRangeChanged(0, currentItemCount)
            rv?.layoutManager?.onRestoreInstanceState(rvState)
            rv?.scrollToPosition(mainActivity.gameRVPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rvState = rv?.layoutManager?.onSaveInstanceState()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null)
            rv?.layoutManager?.onRestoreInstanceState(rvState)
    }

    override fun onResume() {
        super.onResume()
        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                mainActivity.gameRVPosition =
                    layoutManager.findFirstVisibleItemPosition()
                setupInfiniteScroll(dy, layoutManager)
            }
        })
    }

    private fun observeLoadStatusForLoadMore(statusLD: LiveData<LoadStatus>) {
        statusLD.observe(viewLifecycleOwner) {
            canLoadMore = when (it) {
                LoadStatus.LOADING -> {
                    false
                }
                LoadStatus.DONE -> {
                    true
                }
                LoadStatus.ERROR -> {
                    false
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setupInfiniteScroll(dy: Int, lm: LinearLayoutManager) {
        if (dy > 0) { //check for scroll down
            visibleItemCount = lm.childCount
            totalItemCount = lm.itemCount
            firstVisibleItemPos = mainActivity.gameRVPosition

            if (canLoadMore) {
                if ((visibleItemCount + firstVisibleItemPos) >= totalItemCount) {
                    currentPage += 1
                    gameVM.fetchGameList(listOf(teamId), currentPage)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}