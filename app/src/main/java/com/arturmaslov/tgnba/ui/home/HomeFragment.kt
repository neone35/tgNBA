package com.arturmaslov.tgnba.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.databinding.FragmentHomeBinding
import com.arturmaslov.tgnba.ui.UiHelper
import com.arturmaslov.tgnba.ui.main.MainActivity
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home), UiHelper {

    private lateinit var binding: FragmentHomeBinding
    private val homeVM: HomeVM by viewModel()
    private var rv: RecyclerView? = null
    private var rvState: Parcelable? = null
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (requireActivity() as MainActivity)
        homeVM.updateLocalTeamList()
        Logger.i("Creating HomeFragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.i("HomeFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        rv = binding.rvTeamList

        setListeners()
        setObservers()
    }

    override fun setListeners() {
        binding.btnSort.setOnClickListener {
            showSortPopup(it)
        }
    }

    override fun setObservers() {
        mainActivity.observeApiStatus(homeVM.extLoadStatus)
        mainActivity.observeRepositoryResponse(homeVM.remoteResponse)

        homeVM.extTeamList.observe(viewLifecycleOwner) {
            setTeamList(it)
        }
        homeVM.extTeamSortOption.observe(viewLifecycleOwner) {
            binding.btnSort.text = it
        }
    }

    // F.onCreate();
    // F.onCreateView();
    // F.onViewCreated();
    // F.onActivityCreated();
    // F.onViewStateRestored();
    // F.onStart();
    // F.onResume();

    override fun onStart() {
        super.onStart()
        mainActivity.slideUpBottomNav()
    }

    private fun setTeamList(teamList: List<Team?>?) {
        val mutableTeamList = teamList?.toMutableList()
        val currentItemCount = rv?.adapter?.itemCount
        if (currentItemCount == null || currentItemCount == 0) {
            // create new adapter with initial data
            val adapter = TeamListAdapter(mutableTeamList, homeVM)
            rv?.adapter = adapter
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.scrollToPosition(mainActivity.teamRVPosition)
        } else {
            // update existing adapter with updated data
            rvState = rv?.layoutManager?.onSaveInstanceState()
            val currentAdapter = rv?.adapter as? TeamListAdapter
            currentAdapter?.updateTeamList(teamList)
            currentAdapter?.notifyItemRangeChanged(0, currentItemCount)
            rv?.layoutManager?.onRestoreInstanceState(rvState)
            rv?.scrollToPosition(mainActivity.teamRVPosition)
        }
    }

    private fun showSortPopup(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.menu_sort)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.menu_item_name -> {
                    homeVM.sortTeamList(TeamSortOption.NAME)
                    binding.btnSort.text = getString(R.string.name_title)
                }
                R.id.menu_item_city -> {
                    homeVM.sortTeamList(TeamSortOption.CITY)
                    binding.btnSort.text = getString(R.string.city_title)
                }
                R.id.menu_item_conference -> {
                    homeVM.sortTeamList(TeamSortOption.CONFERENCE)
                    binding.btnSort.text = getString(R.string.conference_title)
                }
            }
            true
        }
        popup.show()
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
                mainActivity.teamRVPosition =
                    (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstCompletelyVisibleItemPosition()
            }
        })
    }

}