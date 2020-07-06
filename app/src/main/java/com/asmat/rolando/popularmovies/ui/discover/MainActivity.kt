package com.asmat.rolando.popularmovies.ui.discover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.asmat.rolando.popularmovies.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main screen the user lands on upon launch.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val set = setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_saved)
        val appBarConfiguration = AppBarConfiguration(set)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

//    override val activityTitle: String
//        get() = getString(R.string.discover)
//
//    override val pagerAdapter: BaseSectionsPagerAdapter
//        get() = DiscoverPagerAdapter(supportFragmentManager, this)
}