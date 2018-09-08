package com.asmat.rolando.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.adapters.BaseSectionsPagerAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;

abstract class BaseActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    static private final String CURRENT_TAB = "current_Tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        DatabaseManager.INSTANCE.setInstance(this);
        setupToolBar();
        setupViewPager();
        if (savedInstanceState != null) {
            int tab = savedInstanceState.getInt(CURRENT_TAB);
            ViewPager viewPager = findViewById(R.id.container);
            viewPager.setCurrentItem(tab);
        }
        setupDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(this, SearchResultsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // DON'T call this! it causes the fragment manager to mess up and use the
        // wrong fragment on views. Probably due to the way that it's setting/getting
        // them from memory. According to docs, it's using the item's layout id, of which
        // there is only 1 so that's causing page 0 and 1 to be the same :(
        // super.onSaveInstanceState(outState);

        int selectedTab = tabLayout.getSelectedTabPosition();
        outState.putInt(CURRENT_TAB, selectedTab);
    }

    //region Abstract

    abstract String getActivityTitle();
    abstract BaseSectionsPagerAdapter getPagerAdapter();

    //endregion

    //region Private

    private void setupDrawer() {
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        handleMenuItemClicked(menuItem.getItemId());
                        return true;
                    }
                });
        if (this instanceof DiscoverActivity) {
            navigationView.setCheckedItem(R.id.discover);
        } else if (this instanceof MyListsActivity) {
            navigationView.setCheckedItem(R.id.my_lists);
        }
    }

    private void handleMenuItemClicked(int id) {
        switch (id) {
            case R.id.my_lists:
                launchMyLists();
                break;
            case R.id.discover:
                launchDiscover();
                break;
        }
    }

    private void launchDiscover() {
        if (this instanceof DiscoverActivity) { return; }
        launchActivity(DiscoverActivity.class);
    }

    private void launchMyLists() {
        if (this instanceof MyListsActivity) { return; }
        launchActivity(MyListsActivity.class);
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getActivityTitle());
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        BaseSectionsPagerAdapter adapter = getPagerAdapter();
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    //endregion

}
