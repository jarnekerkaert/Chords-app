package com.hogent.tictac

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.viewmodel.SongViewModel
import com.hogent.tictac.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)

        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        userViewModel.databaseUser.observe(this, Observer<Model.User?> {
            if (it != null) {
                navigationView.getHeaderView(0)?.appCompatTextView!!.text = it.name
                navController.navigate(R.id.songListFragment)
            } else {
                navigationView.getHeaderView(0)?.appCompatTextView!!.text = ""
            }
            reloadMenu()
        })

        userViewModel.userToast.observe(this, Observer {
            if (it != null) {
                toast(it).show()
            }
        })

        songViewModel.songToast.observe(this, Observer {
            if (it != null) {
                toast(it).show()
            }
        })
    }

    private fun reloadMenu() {
        val menu = navigationView.menu

        if (userViewModel.databaseUser.value != null) {
            menu.findItem(R.id.logout).isVisible = true
            menu.findItem(R.id.login).isVisible = false
        } else {
            menu.findItem(R.id.logout).isVisible = false
            menu.findItem(R.id.login).isVisible = true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true;
        drawerLayout.closeDrawers();

        when (item.itemId) {
            R.id.logout -> {
                logout()
                return true
            }
            R.id.login -> {
                navController.navigate(R.id.action_songListFragment_to_loginFragment)
                return true
            }
            R.id.songList -> {
                navController.navigate(R.id.songListFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        userViewModel.userRepository.nukeUsers()
        getSharedPreferences("user", Context.MODE_PRIVATE)
            .edit()
            .remove("token")
            .apply()
        navController.navigate(R.id.songListFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}