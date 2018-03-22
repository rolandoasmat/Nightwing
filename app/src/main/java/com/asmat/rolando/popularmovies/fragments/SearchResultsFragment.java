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
import android.widget.LinearLayout;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.activities.MovieDetailActivity;
import com.asmat.rolando.popularmovies.adapters.MoviesGridAdapter;
import com.asmat.rolando.popularmovies.managers.MovieApiManager;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;
import com.asmat.rolando.popularmovies.utilities.ViewUtils;

import java.util.ArrayList;

/**
 * Created by rolandoasmat on 3/21/18.
 */

public class SearchResultsFragment extends Fragment implements MovieAdapterOnClickHandler {

    private MoviesGridAdapter mMoviesGridAdapter;
    private Context mContext;
    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> fetchMoviesCallbacks;
    private String searchQuery;
    private int page;
    private RecyclerView mMoviesGrid;
    private LinearLayout mNoInternetView;
    private boolean fetchingMovies = false;
    private int loaderID = 3958293;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mMoviesGrid = rootView.findViewById(R.id.rv_movie_grid);
        mNoInternetView = rootView.findViewById(R.id.no_internet_layout);
        GridLayoutManager mMoviesGridLayoutManager;
        if (page == 1) {
            int numOfCol = ViewUtils.calculateNumberOfColumns(mContext);
            mMoviesGridLayoutManager = new GridLayoutManager(mContext, numOfCol);
            mMoviesGridAdapter = new MoviesGridAdapter(this);
            mMoviesGrid.setHasFixedSize(true);
            mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
            mMoviesGrid.setAdapter(mMoviesGridAdapter);
            setFetchMoviesLoaderCallback();
            fetchMovies();
        } else {
            mMoviesGridLayoutManager = new GridLayoutManager(mContext, ViewUtils.calculateNumberOfColumns(mContext));
            mMoviesGrid.setLayoutManager(mMoviesGridLayoutManager);
            mMoviesGrid.setAdapter(mMoviesGridAdapter);
        }
        mMoviesGrid.addOnScrollListener(createScrollListener(mMoviesGridLayoutManager));
        mMoviesGrid.setNestedScrollingEnabled(false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!NetworkUtils.isOnline(getContext())) {
            // User has no internet
            mMoviesGrid.setVisibility(View.GONE);
            mNoInternetView.setVisibility(View.VISIBLE);
        } else {
            // Internet connection established
            mMoviesGrid.setVisibility(View.VISIBLE);
            mNoInternetView.setVisibility(View.GONE);
        }
    }

    public SearchResultsFragment() {
        page = 1;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        mMoviesGridAdapter.setMovies(new ArrayList<Movie>());
        this.page = 1;
        fetchMovies();
    }

    private RecyclerView.OnScrollListener createScrollListener(final GridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && !fetchingMovies) { // User is scrolling down
                    int positionOfLastItem = layoutManager.getItemCount() - 1;
                    int currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (currentPositionOfLastVisibleItem >= positionOfLastItem - 5) {
                        fetchMovies();
                    }
                }
            }
        };
    }

    private void fetchMovies() {
        fetchingMovies = true;
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(loaderID, null, fetchMoviesCallbacks);
    }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void setFetchMoviesLoaderCallback() {
        fetchMoviesCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {

            @Override
            public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<ArrayList<Movie>>(mContext) {

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        try {
                            return MovieApiManager.fetchSearchMovies(searchQuery, page);
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
                if (data == null) {
                    // TODO show error message
                } else {
                    if (page == 1) {
                        mMoviesGridAdapter.setMovies(data);
                    } else {
                        mMoviesGridAdapter.addMovies(data);
                    }
                    page++;
                    fetchingMovies = false;
                }
                getLoaderManager().destroyLoader(loaderID);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
            }
        };
    }
}