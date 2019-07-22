package com.asmat.rolando.popularmovies.ui.common

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.discover.DiscoverActivity
import com.asmat.rolando.popularmovies.ui.mylists.MyListsActivity
import com.asmat.rolando.popularmovies.ui.search.SearchResultsActivity

/**
 * Base Activity that contains the following common behavior of:
 * - Housing a FragmentPagerAdapter
 * - Implementing a side menu, DrawerLayout
 * - Having a search bar at the top
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val CURRENT_TAB = "current_Tab"
    }

    private var tabLayout: TabLayout? = null

    //region Abstract

    internal abstract val activityTitle: String
    internal abstract val pagerAdapter: BaseSectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setupToolBar()
        setupViewPager()
        setupDrawer()
        if (savedInstanceState != null) {
            val tab = savedInstanceState.getInt(CURRENT_TAB)
            val viewPager = findViewById<ViewPager>(R.id.container)
            viewPager.currentItem = tab
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchResultsActivity::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                openDrawer()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // DON'T call this! it causes the fragment manager to mess up and use the
        // wrong fragment on views. Probably due to the way that it's setting/getting
        // them from memory. According to docs, it's using the item's layout id, of which
        // there is only 1 so that's causing page 0 and 1 to be the same :(
        // super.onSaveInstanceState(outState);

        tabLayout?.selectedTabPosition?.let { selectedTab ->
            outState.putInt(CURRENT_TAB, selectedTab)
        }
    }

    //endregion

    //region Private

    private fun setupDrawer() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            handleMenuItemClicked(menuItem.itemId)
            true
        }
        if (this is DiscoverActivity) {
            navigationView.setCheckedItem(R.id.discover)
        } else if (this is MyListsActivity) {
            navigationView.setCheckedItem(R.id.my_lists)
        }
    }

    private fun handleMenuItemClicked(id: Int) {
        when (id) {
            R.id.my_lists -> launchMyLists()
            R.id.discover -> launchDiscover()
        }
    }

    private fun launchDiscover() {
        if (this is DiscoverActivity) {
            return
        }
        launchActivity(DiscoverActivity::class.java)
    }

    private fun launchMyLists() {
        if (this is MyListsActivity) {
            return
        }
        launchActivity(MyListsActivity::class.java)
    }

    private fun launchActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun setupToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = activityTitle
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun setupViewPager() {
        val adapter = pagerAdapter
        val viewPager = findViewById<ViewPager>(R.id.container)
        viewPager.adapter = adapter
        tabLayout = findViewById(R.id.tabs)
        tabLayout?.setupWithViewPager(viewPager)
    }

    private fun openDrawer() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.openDrawer(GravityCompat.START)
    }



    //endregion

}
