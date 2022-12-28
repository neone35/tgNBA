package com.arturmaslov.tgnba

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arturmaslov.tgnba.data.source.ApiStatus
import com.arturmaslov.tgnba.databinding.ActivityMainBinding
import com.arturmaslov.tgnba.utils.ToastUtils
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainVM: MainVM by viewModel()
    private var navController: NavController? = null
    private var progressAnimator: ObjectAnimator = ObjectAnimator()

    private var disableBackCallback: OnBackPressedCallback? = null
    private var hideFragment: Boolean = false
    private var hideLoadingPie: Boolean = false

    var teamRVPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMainInclude.toolbarIntro)
        binding.toolbarMainInclude.tvAppVer.text = "v${BuildConfig.VERSION_NAME}"
        supportActionBar?.setDisplayShowTitleEnabled(false)
        loadLoadingPieAnimation()
        setUpNavigation()
        observeApiStatus(mainVM.extStatus)
        observeRepositoryResponse(mainVM.sharedResponse)
        observeRepositoryResponse(mainVM.remoteResponse)

        mainVM.extInternetAvailable.observe(this) { intAvailable ->
            Logger.d("Internet is $intAvailable")
            if (intAvailable == true) {
                if (navController?.currentDestination?.id != R.id.frag_home)
                    navController?.navigate(R.id.action_frag_home_self)
            } else {
                ToastUtils.updateWarning(this, getString(R.string.no_internet))
                showLoading(true)
            }
        }

        // Impossible to go back for this activity and all its children if loading
        disableBackCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                return
            }
        }
        onBackPressedDispatcher.addCallback(this, disableBackCallback as OnBackPressedCallback)

    }

    private fun setUpNavigation() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.bnvMain.setupWithNavController(navController!!)
    }

    private fun loadLoadingPieAnimation() {
        progressAnimator = ObjectAnimator.ofInt(binding.pbLoading, "progress", 0, 101)
        progressAnimator.duration = 2500
        progressAnimator.repeatCount = ObjectAnimator.INFINITE
    }

    private fun observeApiStatus(statusLD: LiveData<ApiStatus>) {
        statusLD.observe(this) {
            // show loading animation
            when (it) {
                ApiStatus.LOADING -> {
                    showLoading(true)
                    disableBackCallback?.isEnabled = true
                }
                ApiStatus.DONE -> {
                    showLoading(false)
                    disableBackCallback?.isEnabled = false
                }
                ApiStatus.ERROR -> {
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

    private fun observeRepositoryResponse(repoResponseLD: LiveData<String?>) {
        println("observeRepositoryResponse called")
        if (!repoResponseLD.hasObservers()) {
            repoResponseLD.observe(this) {
                Logger.i("observeRepositoryResponse: $it")
                ToastUtils.updateWarning(this, "$it")
            }
        }
    }

    private fun showLoading(showLoading: Boolean) {
        if (showLoading) {
            progressAnimator.start()
            binding.pbLoading.visibility = View.VISIBLE
            if (hideFragment) binding.navHostFragmentActivityMain.visibility = View.INVISIBLE
            if (hideLoadingPie) binding.pbLoading.visibility = View.INVISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.navHostFragmentActivityMain.visibility = View.VISIBLE
            progressAnimator.end()
        }
    }

    fun slideUpBottomNav() {
        Logger.d("sliding up bottom nav")
        val layoutParams = binding.bnvMain.layoutParams as CoordinatorLayout.LayoutParams
        val bottomViewNavigationBehavior = layoutParams.behavior as HideBottomViewOnScrollBehavior
        bottomViewNavigationBehavior.slideUp(binding.bnvMain)
    }
}