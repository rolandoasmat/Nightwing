package com.asmat.rolando.nightwing.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main screen the user lands on upon launch.
 */
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.home, R.navigation.search, R.navigation.saved)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = (bottomNavigationView)?.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_fragment,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller?.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.label == "Movie Details" || destination.label == "Cast Details" || destination.label == "TV Show Details") {
                    supportActionBar?.hide()
                } else {
                    supportActionBar?.show()
                }
            }
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}