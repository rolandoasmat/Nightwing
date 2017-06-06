package com.asmat.rolando.popularmovies.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.adapters.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    static private final String CURRENT_PAGE_KEY = "current_page_key";
    static private final String TAG = "RA:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        if(savedInstanceState != null) {
            Log.v(TAG, "savedInstanceState was not null");
            int page = savedInstanceState.getInt(CURRENT_PAGE_KEY);
            Log.v(TAG, "got page: "+page+" from bundle.");
            mViewPager.setCurrentItem(page);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // DON'T call this! it causes the fragment manager to mess up and user the
        // wrong fragment on views. Probably due to the way that it's setting/getting
        // them from memory. According to docs, it's using the item's layout id, of which
        // there is only 1 so that's causing page 0 and 1 to be the same :(
        //super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");
        int currentShownItem = mTabLayout.getSelectedTabPosition();
        Log.v(TAG, "saving page: "+currentShownItem+" in bundle.");
        outState.putInt(CURRENT_PAGE_KEY, currentShownItem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        this.mSectionsPagerAdapter = null;
        this.mTabLayout = null;
    }
}