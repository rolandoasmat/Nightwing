package com.asmat.rolando.popularmovies.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.fragments.SearchResultsFragment;


public class SearchResultsActivity extends AppCompatActivity {
    SearchResultsFragment mResultsGrid;
    SearchView mSearchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setupToolbar();
        setupFragment();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    // Enable Back arrow
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupFragment() {
        mResultsGrid = (SearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.results_grid);
    }

    // Handle the search query
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchview.setQuery(query, false);
            mResultsGrid.setSearchQuery(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_results, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchview = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        mSearchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchview.setIconified(false);
        mSearchview.setIconifiedByDefault(true);
        mSearchview.setSubmitButtonEnabled(true);
        return true;
    }
}