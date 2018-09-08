package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class BaseSectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    BaseSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return getFragments().length;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragments()[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getPageTitles()[position];
    }

    String getString(int id) {
        return context.getString(id);
    }

    //region Abstract
    abstract Fragment[] getFragments();
    abstract String[] getPageTitles();
    //endregion

}
