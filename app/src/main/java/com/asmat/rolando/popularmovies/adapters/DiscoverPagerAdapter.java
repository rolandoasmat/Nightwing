package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.fragments.MovieGridFragment;
import com.asmat.rolando.popularmovies.models.RequestType;

public class DiscoverPagerAdapter extends BaseSectionsPagerAdapter {

    public DiscoverPagerAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    Fragment[] getFragments() {
        MovieGridFragment popular = new MovieGridFragment();
        popular.setTypeOfMovies(RequestType.MOST_POPULAR);
        MovieGridFragment topRated = new MovieGridFragment();
        topRated.setTypeOfMovies(RequestType.TOP_RATED);
        MovieGridFragment nowPlaying = new MovieGridFragment();
        nowPlaying.setTypeOfMovies(RequestType.NOW_PLAYING);
        return new Fragment[]{popular, topRated, nowPlaying};
    }

    @Override
    String[] getPageTitles() {
        String popular = getString(R.string.most_popular);
        String topRated = getString(R.string.top_rated);
        String nowPlaying = getString(R.string.now_playing);
        return new String[]{popular, topRated, nowPlaying};
    }
}
