package com.arturmaslov.tgnba.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arturmaslov.tgnba.BuildConfig
import com.arturmaslov.tgnba.R
import com.arturmaslov.tgnba.data.source.LoadStatus
import com.arturmaslov.tgnba.databinding.ActivityMainBinding
import com.arturmaslov.tgnba.ui.UiHelper
import com.arturmaslov.tgnba.utils.ToastUtils
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UiHelper {

    private lateinit var binding: ActivityMainBinding
    private val mainVM: MainVM by viewModel()
    private var navHostFragment: NavHostFragment? = null
    private var navController: NavController? = null

    private var disableBackCallback: OnBackPressedCallback? = null
    var teamRVPosition: Int = 0
    var playerRVPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpDebugLogging()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMainInclude.toolbarIntro)
        binding.toolbarMainInclude.tvAppVer.text = "v${BuildConfig.VERSION_NAME}"
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setUpNavigation()
        setObservers()
        setListeners()
    }

    override fun setObservers() {
        observeInternetAvailability()
        observeApiStatus(mainVM.extLoadStatus)
        observeRepositoryResponse(mainVM.remoteResponse)
    }

    override fun setListeners() {
        // Impossible to go back for this activity and all its children if loading when enabled
        disableBackCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                return
            }
        }
        onBackPressedDispatcher.addCallback(this, disableBackCallback as OnBackPressedCallback)
    }

    private fun observeInternetAvailability() {
        mainVM.extInternetAvailable.observe(this) { intAvailable ->
            Logger.d("Internet is $intAvailable")
            if (intAvailable == false) {
                ToastUtils.updateWarning(this, getString(R.string.no_internet))
                showLoading(true)
            }
        }
    }

    private fun setUpNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frag_container_main) as NavHostFragment
        navController = navHostFragment!!.navController
        binding.bnvMain.setupWithNavController(navController!!)
    }

    fun observeApiStatus(statusLD: LiveData<LoadStatus>) {
        statusLD.observe(this) {
            Logger.d("api status is $it")
            when (it) {
                LoadStatus.LOADING -> {
                    showLoading(true)
                    disableBackCallback?.isEnabled = true
                }
                LoadStatus.DONE -> {
                    showLoading(false)
                    disableBackCallback?.isEnabled = false
                }
                LoadStatus.ERROR -> {
                    showLoading(false)
                    Logger.e("Failure: $it")
                    disableBackCallback?.isEnabled = false
                }
                else -> {
                    showLoading(false)
                    disableBackCallback?.isEnabled = false
                }
            }
        }
    }

    fun observeRepositoryResponse(repoResponseLD: LiveData<String?>) {
        println("observeRepositoryResponse called")
        if (!repoResponseLD.hasObservers()) {
            repoResponseLD.observe(this) {
                Logger.i("observeRepositoryResponse: $it")
                ToastUtils.updateWarning(this, "$it")
            }
        }
    }

    private fun showLoading(showLoading: Boolean) {
        Logger.d("showing loading is $showLoading")
        if (showLoading) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.fragContainerMain.visibility = View.VISIBLE
        }
    }

    fun slideUpBottomNav() {
        Logger.d("sliding up bottom nav")
        val layoutParams = binding.bnvMain.layoutParams as CoordinatorLayout.LayoutParams
        val bottomViewNavigationBehavior = layoutParams.behavior as HideBottomViewOnScrollBehavior
        bottomViewNavigationBehavior.slideUp(binding.bnvMain)
    }

    private fun setUpDebugLogging() {
        Logger.clearLogAdapters()
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
    }
}