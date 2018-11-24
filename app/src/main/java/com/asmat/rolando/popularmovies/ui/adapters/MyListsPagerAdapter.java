package com.asmat.rolando.popularmovies.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.ui.fragments.FavoriteMoviesGridFragment;
import com.asmat.rolando.popularmovies.ui.fragments.WatchLaterMoviesGridFragment;

public class MyListsPagerAdapter extends BaseSectionsPagerAdapter {

    public MyListsPagerAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    Fragment[] getFragments() {
        FavoriteMoviesGridFragment favorites = new FavoriteMoviesGridFragment();
        WatchLaterMoviesGridFragment watchLater = new WatchLaterMoviesGridFragment();
        return new Fragment[]{favorites, watchLater};
    }

    @Override
    String[] getPageTitles() {
        String popular = getString(R.string.favorites);
        String topRated = getString(R.string.watch_later);
        return new String[]{popular, topRated};
    }
}
