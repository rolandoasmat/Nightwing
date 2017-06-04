package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return NUM_OF_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MovieGridFragment popularMoviesFragment = new MovieGridFragment();
                popularMoviesFragment.setTypeOfMovies(RequestType.MOST_POPULAR);
                return popularMoviesFragment;
            case 1:
                MovieGridFragment topRatedMoviesFragment = new MovieGridFragment();
                topRatedMoviesFragment.setTypeOfMovies(RequestType.TOP_RATED);
                return topRatedMoviesFragment;
            case 2:
                return new FavoriteMoviesGridFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.most_popular);
            case 1:
                return context.getString(R.string.top_rated);
            case 2:
                return context.getString(R.string.favorites);
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
