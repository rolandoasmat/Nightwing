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

import java.util.ArrayList;

/**
 * Created by rolandoasmat on 5/29/17.
 */

public class MovieGridFragment extends Fragment implements MovieAdapterOnClickHandler {

    private RecyclerView mMoviesGrid;
    private MoviesGridAdapter mMoviesGridAdapter;
    private GridLayoutManager mMoviesGridLayoutManager;
    private Context context;
    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> fetchMoviesCallbacks;
    private int typeOfMovies;
    private int page;
    private final String PAGE_KEY = "page_key";

    public MovieGridFragment() {
        page = 1;
    }

    public void setTypeOfMovies(int typeOfMovies) {
        this.typeOfMovies = typeOfMovies;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getBaseContext();
    }

    // TODO savedInstanceState is ALWAYS NULL :(. Using Arguments instead to save state
    // TODO save state upon a rotation
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mMoviesGrid = (RecyclerView) rootView.findViewById(R.id.rv_movie_grid);
        //Bundle args = get
        if(page == 1) {
            mMoviesGridLayoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
            mMoviesGridAdapter = new MoviesGridAdapter(this);
            mMoviesGrid.setHasFixedSize(true);
            mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
            mMoviesGrid.setAdapter(mMoviesGridAdapter);
            setFetchMoviesLoaderCallback();
            fetchMovies();
        } else {
            mMoviesGridLayoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
            mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
            mMoviesGrid.setAdapter(mMoviesGridAdapter);
        }
        mMoviesGrid.addOnScrollListener(mOnScrollListener);
        mMoviesGrid.setNestedScrollingEnabled(false);
        return rootView;
    }
    private boolean fetchingMovies = false;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if(dy > 0 && !fetchingMovies) { // User is scrolling down
                int positionOfLastItem = mMoviesGridLayoutManager.getItemCount()-1;
                int currentPositionOfLastVisibleItem = mMoviesGridLayoutManager.findLastVisibleItemPosition();
                if(currentPositionOfLastVisibleItem == positionOfLastItem - 2){
                    fetchMovies();
                }
            }
        }
    };

    private void fetchMovies() {
        fetchingMovies = true;
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        if(page == 1) {
            loaderManager.initLoader(typeOfMovies, null, fetchMoviesCallbacks);
        } else {
            loaderManager.restartLoader(typeOfMovies, null, fetchMoviesCallbacks);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = new Bundle();
        onSaveInstanceState(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE_KEY, page);
    }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void setFetchMoviesLoaderCallback() {

        fetchMoviesCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {

            @Override
            public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<ArrayList<Movie>>(context) {

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
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
            public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
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
                    fetchingMovies = false;
                }
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

            }
        };

    }
}