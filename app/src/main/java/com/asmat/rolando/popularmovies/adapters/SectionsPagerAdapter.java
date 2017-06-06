package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.fragments.FavoriteMoviesGridFragment;
import com.asmat.rolando.popularmovies.fragments.MovieGridFragment;
import com.asmat.rolando.popularmovies.models.RequestType;

/**
 * Created by rolandoasmat on 5/29/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_OF_PAGES = 3;
    static private final String TAG = "RA:SectionsPagerAdapter";
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        Log.v(TAG, "SectionsPagerAdapter constructor.");
    }

    @Override
    public int getCount() {
        Log.v(TAG, "getCount");
        Log.v(TAG, "returning: "+NUM_OF_PAGES);
        return NUM_OF_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem");
        Log.v(TAG, "position: "+position);
        switch (position) {
            case 0:
                MovieGridFragment popularMoviesFragment = new MovieGridFragment();
                popularMoviesFragment.setTypeOfMovies(RequestType.MOST_POPULAR);
                Log.v(TAG, "returning: "+popularMoviesFragment);
                return popularMoviesFragment;
            case 1:
                MovieGridFragment topRatedMoviesFragment = new MovieGridFragment();
                topRatedMoviesFragment.setTypeOfMovies(RequestType.TOP_RATED);
                Log.v(TAG, "returning: "+topRatedMoviesFragment);
                return topRatedMoviesFragment;
            case 2:
                FavoriteMoviesGridFragment favoritesFragment = new FavoriteMoviesGridFragment();
                Log.v(TAG, "returning: "+favoritesFragment);
                return favoritesFragment;
        }
        Log.v(TAG, "returning: null");
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.v(TAG, "getPageTitle");
        Log.v(TAG, "position: "+position);
        String title;
        switch (position) {
            case 0:
                title = context.getString(R.string.most_popular);
                break;
            case 1:
                title = context.getString(R.string.top_rated);
                break;
            case 2:
                title =  context.getString(R.string.favorites);
                break;
            default:
                title = null;
        }
        Log.v(TAG, "returning: "+title);
        return title;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.v(TAG, "instantiateItem");
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.v(TAG, "destroyItem");
        super.destroyItem(container, position, object);
    }
}
