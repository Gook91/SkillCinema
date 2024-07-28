package com.example.skillcinema.presentation.welcomeActivity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityWelcomeBinding
import com.example.skillcinema.presentation.MainActivity
import com.example.skillcinema.presentation.lists.adapters.simple.WelcomeAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WelcomeActivity : AppCompatActivity() {

    private var _binding: ActivityWelcomeBinding? = null
    private val binding get() = _binding!!

    private val welcomeViewModel: WelcomeViewModel by viewModels {
        (application as App).appComponent.welcomeViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hintScreens = listOf(
            getString(R.string.hello_find_out_about_premieres) to R.drawable.hello_premiers,
            getString(R.string.hello_create_collections) to R.drawable.hello_collect,
            getString(R.string.hello_share_with_friends) to R.drawable.hello_share
        )

        binding.tipsPager.adapter = WelcomeAdapter(hintScreens) { position ->
            if (position == hintScreens.lastIndex)
                closeWelcomeScreen()
            else
                binding.tipsPager.currentItem = position + 1
        }
        binding.tipsPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tipsIndicator, binding.tipsPager) { _, _ ->
        }.attach()

        binding.skipScreen.setOnClickListener {
            closeWelcomeScreen()
        }

        welcomeViewModel.isFirstLaunchFlow.onEach { isFirstLaunch ->
            if (!isFirstLaunch) closeWelcomeScreen()
        }.launchIn(lifecycleScope)

        updateTopPaddingFromInsets(binding.root)

        setColorToNavBar()
    }

    private fun closeWelcomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun updateTopPaddingFromInsets(rootView: View) {
        val actionBarHeight = getActionBarHeight()
        window?.decorView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { _, insets ->
                val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                rootView.updatePadding(top = actionBarHeight + statusBarHeight)
                insets
            }
        }
    }

    private fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (theme.resolveAttribute(
                android.R.attr.actionBarSize,
                tv,
                true
            )
        ) {
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else 0
    }

    private fun setColorToNavBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}