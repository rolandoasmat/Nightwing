package com.asmat.rolando.popularmovies.ui.activities;


import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.ui.adapters.pager.BaseSectionsPagerAdapter;
import com.asmat.rolando.popularmovies.ui.adapters.pager.DiscoverPagerAdapter;

public class DiscoverActivity extends BaseActivity {

    @Override
    String getActivityTitle() {
        return getString(R.string.discover);
    }

    @Override
    BaseSectionsPagerAdapter getPagerAdapter() {
        return new DiscoverPagerAdapter(getSupportFragmentManager(),this);
    }
}