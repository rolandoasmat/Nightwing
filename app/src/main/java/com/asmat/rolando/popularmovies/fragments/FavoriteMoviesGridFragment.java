package com.asmat.rolando.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.activities.MovieDetailActivity;
import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.data.PopularMoviesContract;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.utilities.ViewUtils;

import java.util.ArrayList;

/**
 * Created by rolandoasmat on 5/29/17.
 */

public class FavoriteMoviesGridFragment extends Fragment
        implements MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Context context;

    private final int LOADER_ID = 20349;

    public FavoriteMoviesGridFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid,container, false);
        mMoviesGrid = (RecyclerView) rootView.findViewById(R.id.rv_movie_grid);
        mMoviesGridLayoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
        mMoviesGridAdapter = new MoviesGridAdapter(this);
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

    private void fetchMovies() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = PopularMoviesContract.FavoritesEntry.CONTENT_URI;
        return new CursorLoader(getActivity().getBaseContext(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Movie> favoriteMovies = new ArrayList<>();
        while(data.moveToNext()) {
            favoriteMovies.add(Movie.getMovieFromCursorEntry(data));
        }
        data.close();
        mMoviesGridAdapter.setMovies(favoriteMovies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

}