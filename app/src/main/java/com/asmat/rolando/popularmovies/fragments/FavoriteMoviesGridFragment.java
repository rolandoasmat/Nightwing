package com.asmat.rolando.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asmat.rolando.popularmovies.R;

/**
 * Created by rolandoasmat on 5/29/17.
 */

public class FavoriteMoviesGridFragment extends Fragment {

    public FavoriteMoviesGridFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid,container, false);
        return rootView;
    }
}
