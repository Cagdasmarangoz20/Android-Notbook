package com.cagdasmarangoz.notes.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cagdasmarangoz.notes.R
import com.cagdasmarangoz.notes.core.ViewBindingActivity
import com.cagdasmarangoz.notes.databinding.ActivityMainBinding


class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationComponent()
    }

    private fun initNavigationComponent() {
        val navController = findNavController(R.id.nav_host_fragment)
        withVB {
            navView.setupWithNavController(navController)
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(navController, drawerLayout)
        }
    }

}

