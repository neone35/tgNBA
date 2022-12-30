package com.arturmaslov.tgnba.ui.player

import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Player
import com.arturmaslov.tgnba.databinding.FragmentPlayerBinding
import com.arturmaslov.tgnba.ui.UiHelper
import com.arturmaslov.tgnba.ui.main.MainActivity
import com.arturmaslov.tgnba.utils.Constants
import com.arturmaslov.tgnba.utils.ToastUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(R.layout.fragment_player), UiHelper {

    private lateinit var binding: FragmentPlayerBinding
    private val playerVM: PlayerVM by viewModel()
    private var rv: RecyclerView? = null
    private var rvState: Parcelable? = null
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (requireActivity() as MainActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)
        rv = binding.rvPlayerList

        setListeners()
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        mainActivity.controlBottomNav(Constants.SHOW)
        mainActivity.controlBottomNav(Constants.SLIDE_UP)
    }

    override fun setListeners() {
        binding.etPlayerSearch.setOnEditorActionListener { searchTerm, actionId, keyEvent ->
            val imeAction = when (actionId) {
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_SEND,
                EditorInfo.IME_ACTION_GO -> true
                else -> false
            }
            val keyDownEvent = keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER
                    && keyEvent.action == KeyEvent.ACTION_DOWN
            if (imeAction or keyDownEvent) {
                fetchSearchTerm(searchTerm.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun setObservers() {
        mainActivity.observeLoadStatus(playerVM.extLoadStatus)
        mainActivity.observeRepositoryResponse(playerVM.remoteResponse)

        playerVM.extPlayerList.observe(viewLifecycleOwner) {
            setPlayerList(it)
            val tv = binding.tvProvideSearchTerm
            if (!it.isNullOrEmpty())
                tv.visibility = View.GONE
            else
                tv.visibility = View.VISIBLE
        }
        playerVM.extSearchTerm.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                val et = binding.etPlayerSearch
                et.setText(it)
                et.focus()
            }
        }
    }

    // sets cursor to an end
    private fun EditText.focus() {
        requestFocus()
        setSelection(length())
    }

    private fun fetchSearchTerm(searchTerm: String) {
        if (searchTerm.length > 2) {
            playerVM.fetchPlayerList(searchTerm)
        } else {
            ToastUtils.updateWarning(requireActivity(), "Search term must be longer than 2 chars")
        }
    }

    private fun setPlayerList(playerList: List<Player?>?) {
        val mutablePlayerList = playerList?.toMutableList()
        val currentItemCount = rv?.adapter?.itemCount
        if (currentItemCount == null || currentItemCount == 0) {
            // create new adapter with initial data
            val adapter = PlayerListAdapter(mutablePlayerList)
            rv?.adapter = adapter
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.scrollToPosition(mainActivity.playerRVPosition)
        } else {
            // update existing adapter with updated data
            rvState = rv?.layoutManager?.onSaveInstanceState()
            val currentAdapter = rv?.adapter as? PlayerListAdapter
            currentAdapter?.updatePlayerList(playerList)
            currentAdapter?.notifyItemRangeChanged(0, currentItemCount)
            rv?.layoutManager?.onRestoreInstanceState(rvState)
            rv?.scrollToPosition(mainActivity.playerRVPosition)
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
                mainActivity.playerRVPosition =
                    (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstCompletelyVisibleItemPosition()
            }
        })
    }

}