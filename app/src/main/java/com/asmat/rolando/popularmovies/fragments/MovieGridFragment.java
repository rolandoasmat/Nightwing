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
import android.util.Log;
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
 * Created by rolandoasmat on 5/29/17.
 */

public class MovieGridFragment extends Fragment implements MovieAdapterOnClickHandler {

    private MoviesGridAdapter mMoviesGridAdapter;
    private Context mContext;
    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> fetchMoviesCallbacks;
    private int typeOfMovies;
    private int page;
    private RecyclerView mMoviesGrid;
    private LinearLayout mNoInternetView;
    private boolean fetchingMovies = false;
    private final String TAG = "RA:MovieGridFragment:";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(tag(), "onAttach");
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(tag(), "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(tag(), "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mMoviesGrid = (RecyclerView) rootView.findViewById(R.id.rv_movie_grid);
        mNoInternetView = (LinearLayout) rootView.findViewById(R.id.no_internet_layout);
        GridLayoutManager mMoviesGridLayoutManager;
        if(page == 1) {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(tag(), "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(tag(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(tag(), "onResume");
        if(!NetworkUtils.isOnline(getContext())) {
            // User has no internet
            mMoviesGrid.setVisibility(View.GONE);
            mNoInternetView.setVisibility(View.VISIBLE);
        } else {
            // Internet connection established
            mMoviesGrid.setVisibility(View.VISIBLE);
            mNoInternetView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(tag(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(tag(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(tag(), "onDestroyView");
    }

    // DOES NOT get called as user swipes right/left on view pager
    // DOES get called on screen rotation
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(tag(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(tag(), "onDetach");
    }

    public MovieGridFragment() {
        page = 1;
        Log.v(tag(), "MovieGridFragment constructor.");
    }

    public void setTypeOfMovies(int typeOfMovies) {
        this.typeOfMovies = typeOfMovies;
        Log.v(tag(), "setTypeOfMovies");
        Log.v(tag(), "typeOfMovies: "+typeOfMovies);
    }

    private RecyclerView.OnScrollListener createScrollListener(final GridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 && !fetchingMovies) { // User is scrolling down
                    int positionOfLastItem = layoutManager.getItemCount()-1;
                    int currentPositionOfLastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    Log.v(tag(), "onScrolled");
                    Log.v(tag(), "positionOfLastItem: "+positionOfLastItem);
                    Log.v(tag(), "currentPositionOfLastVisibleItem: "+currentPositionOfLastVisibleItem);
                    if(currentPositionOfLastVisibleItem >= positionOfLastItem - 5){
                        fetchMovies();
                    }
                }
            }
        };
    }

    private void fetchMovies() {
        Log.v(tag(), "fetchMovies");
        fetchingMovies = true;
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(typeOfMovies, null, fetchMoviesCallbacks);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(tag(), "onSaveInstanceState");
    }

    @Override
    public void onClick(Movie movie) {
        Log.v(tag(), "onClick");
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.INTENT_EXTRA_TAG, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void setFetchMoviesLoaderCallback() {
        Log.v(tag(), "setFetchMoviesLoaderCallback");

        fetchMoviesCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {

            @Override
            public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<ArrayList<Movie>>(mContext) {

                    @Override
                    protected void onStartLoading() {
                        Log.v(tag(), "onStartLoading");
                        forceLoad();
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        Log.v(tag(), "loadInBackground");
                        try {
                            Log.v(tag(), "typeOfMovies: "+typeOfMovies);
                            Log.v(tag(), "page: "+page);
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
                Log.v(tag(), "onLoadFinished");
                // TODO hide loader
                if(data == null) {
                    // TODO show error message
                } else {
                    if(page == 1) {
                        Log.v(tag(), "setting movies");
                        mMoviesGridAdapter.setMovies(data);
                    } else {
                        Log.v(tag(), "adding movies");
                        mMoviesGridAdapter.addMovies(data);
                    }
                    Log.v(tag(), "data: "+data.toString());
                    page++;
                    fetchingMovies = false;
                }
                getLoaderManager().destroyLoader(typeOfMovies);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
                Log.v(tag(), "onLoaderReset");
            }
        };
    }

    private String tag() {
        return TAG+typeOfMovies;
    }
}