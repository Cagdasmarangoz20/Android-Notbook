package com.cagdasmarangoz.notes.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.cagdasmarangoz.notes.R
import com.cagdasmarangoz.notes.core.ViewBindingActivity
import com.cagdasmarangoz.notes.databinding.ActivityAboutBinding
import com.google.android.material.navigation.NavigationView

class AboutActivity : ViewBindingActivity<ActivityAboutBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationMenu()
    }
    private fun navigationMenu() {

        val navView: NavigationView = findViewById(R.id.idNavView)
        val drawerLayout: DrawerLayout = findViewById(R.id.idAbout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.idHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.id_about ->{
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}