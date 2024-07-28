package com.example.skillcinema.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActionBarBinding
import com.example.skillcinema.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val actionBarBinding: ActionBarBinding by lazy { ActionBarBinding.inflate(layoutInflater) }
    fun setAppBarTitle(title: String) {
        actionBarBinding.toolbarTitle.text = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(actionBarBinding.root, layoutParams)


        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }
        binding.bottomNavView.setOnItemReselectedListener { item ->
            val reselectedDestinationId = item.itemId
            navController.popBackStack(reselectedDestinationId, inclusive = false)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.search_film_fragment)
                supportActionBar?.hide()
            else
                supportActionBar?.show()


            if (destination.id == R.id.main_fragment || destination.id == R.id.profile_fragment)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            else
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

            if (destination.id in ID_FRAGMENTS_WITHOUT_BOTTOM_NAV_BAR)
                binding.bottomNavView.visibility = View.GONE
            else
                binding.bottomNavView.visibility = View.VISIBLE

            setAppBarTitle(destination.label?.toString() ?: "")
            supportActionBar?.setHomeAsUpIndicator(R.drawable.app_nav_back)
        }

        setColorToNavBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment_container)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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

    companion object {
        private val ID_FRAGMENTS_WITHOUT_BOTTOM_NAV_BAR = listOf(
            R.id.search_parameters_fragment, R.id.set_country_fragment,
            R.id.set_genre_fragment, R.id.set_years_fragment,
        )
    }
}