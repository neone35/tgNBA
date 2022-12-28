package com.arturmaslov.tgnba.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeVM: GameVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeVM = ViewModelProvider(this).get(GameVM::class.java)
    }

    // F.onCreate();
    // F.onCreateView();
    // F.onViewCreated();
    // F.onActivityCreated();
    // F.onViewStateRestored();
    // F.onStart();
    // F.onResume();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val textView: TextView = binding.textHome
        homeVM.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.btnSort.setOnClickListener {
            showSortPopup(it)
        }

    }

    private fun showSortPopup(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.menu_sort)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.menu_item_name -> {
                    // TODO: ask HomeVM to sort data
                }
                R.id.menu_item_city -> {
                    // TODO: ask HomeVM to sort data
                }
                R.id.menu_item_conference -> {
                    // TODO: ask HomeVM to sort data
                }
            }
            true
        }

        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}