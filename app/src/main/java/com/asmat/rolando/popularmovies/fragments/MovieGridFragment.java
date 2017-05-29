package com.asmat.rolando.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.activities.MovieDetailActivity;
import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.utilities.ViewUtils;

/**
 * Created by rolandoasmat on 5/29/17.
 */

public class MovieGridFragment extends Fragment implements MovieAdapterOnClickHandler {

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Context context;
    private LoaderManager.LoaderCallbacks<Movie[]> fetchMoviesCallbacks;
    private int typeOfMovies;
    private int page;

    public MovieGridFragment() {
        page = 1;
    }

    public void setTypeOfMovies(int typeOfMovies) {
        this.typeOfMovies = typeOfMovies;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int t = this.typeOfMovies;
        context = getActivity().getBaseContext();
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mMoviesGrid = (RecyclerView) rootView.findViewById(R.id.rv_movie_grid);
        setupRecyclerView();
        setFetchMoviesLoaderCallback();
        getActivity().getSupportLoaderManager().initLoader(typeOfMovies, null, fetchMoviesCallbacks);
        return rootView;
    }

    private void setupRecyclerView() {
        mMoviesGridLayoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
        mMoviesGridAdapter = new MoviesGridAdapter(this);
        mMoviesGrid.setHasFixedSize(true);
        mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
        mMoviesGrid.setAdapter(mMoviesGridAdapter);
    }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void setFetchMoviesLoaderCallback() {

        fetchMoviesCallbacks = new LoaderManager.LoaderCallbacks<Movie[]>() {

            @Override
            public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Movie[]>(context) {

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public Movie[] loadInBackground() {
                        try {
                            return MovieApiManager.fetchMoviesOfType(typeOfMovies, page);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
                // TODO hide loader
                if(data == null) {
                    // TODO show error message
                } else {
                    System.out.print("Movies fetched.");
                    if(page == 1) {
                        mMoviesGridAdapter.setMovies(data);
                    } else {
                        mMoviesGridAdapter.addMovies(data);
                    }
                    page++;
                }
            }

            @Override
            public void onLoaderReset(Loader<Movie[]> loader) {

            }
        };

    }
}