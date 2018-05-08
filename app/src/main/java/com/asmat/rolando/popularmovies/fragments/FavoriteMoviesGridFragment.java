package com.asmat.rolando.popularmovies.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.activities.MovieDetailActivity;
import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.utilities.ViewUtils;


/**
 * Created by rolandoasmat on 5/29/17.
 */

public class FavoriteMoviesGridFragment extends Fragment
        implements MovieAdapterOnClickHandler {

    private MoviesGridAdapter mMoviesGridAdapter;
    private Context context;

    public FavoriteMoviesGridFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridLayoutManager mMoviesGridLayoutManager = null;
        RecyclerView mMoviesGrid;
        View rootView = inflater.inflate(R.layout.fragment_movie_grid,container, false);
        mMoviesGrid = rootView.findViewById(R.id.rv_movie_grid);
        if(mMoviesGridLayoutManager == null) {
            mMoviesGridLayoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
        }
        if(mMoviesGridAdapter == null) {
            mMoviesGridAdapter = new MoviesGridAdapter(this);
        }
        mMoviesGrid.setHasFixedSize(true);
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMovies();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void fetchMovies() {
        LiveData<Movie[]> liveData = DatabaseManager.INSTANCE.getFavoriteMovies();
        liveData.observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                mMoviesGridAdapter.setMovies(movies);
            }
        });
    }


    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

}