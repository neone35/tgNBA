package com.arturmaslov.tgnba.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arturmaslov.tgnba.MainActivity
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.models.Team
import com.arturmaslov.tgnba.databinding.FragmentHomeBinding
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.d("HomeFragment onCreateView")

        binding = FragmentHomeBinding.inflate(layoutInflater)
        rv = binding.rvTeamList

        return super.onCreateView(inflater, container, savedInstanceState)
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

        homeVM.extTeamList.observe(this) {
            setTeamList(it)
        }
        binding.btnSort.setOnClickListener {
            showSortPopup(it)
        }
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
            currentAdapter?.updateOrderList(teamList)
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
                }
                R.id.menu_item_city -> {
                    homeVM.sortTeamList(TeamSortOption.CITY)
                }
                R.id.menu_item_conference -> {
                    homeVM.sortTeamList(TeamSortOption.CONFERENCE)
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}