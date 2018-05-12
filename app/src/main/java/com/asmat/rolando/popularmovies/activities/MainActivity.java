package com.asmat.rolando.popularmovies.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.adapters.SectionsPagerAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    static private final String CURRENT_TAB = "current_Tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager.INSTANCE.setInstance(this);
        setupToolBar();
        setupViewPager();
        if (savedInstanceState != null) {
            int tab = savedInstanceState.getInt(CURRENT_TAB);
            ViewPager viewPager = findViewById(R.id.container);
            viewPager.setCurrentItem(tab);
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager);
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

        int selectedTab = mTabLayout.getSelectedTabPosition();
        outState.putInt(CURRENT_TAB, selectedTab);
    }

}