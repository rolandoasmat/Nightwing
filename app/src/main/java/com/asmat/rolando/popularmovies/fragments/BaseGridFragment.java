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
import com.asmat.rolando.popularmovies.adapters.MoviesGridBaseAdapter;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.asmat.rolando.popularmovies.utilities.ViewUtils;

abstract public class BaseGridFragment extends Fragment
        implements MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private MoviesGridBaseAdapter adapter;
    private Context context;

    public BaseGridFragment() { }

    abstract MoviesGridBaseAdapter getAdapter(MovieAdapterOnClickHandler handler);
    abstract LiveData<Movie[]> getMovieSource();
    //region Lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        recyclerView = rootView.findViewById(R.id.rv_movie_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(context, ViewUtils.calculateNumberOfColumns(context));
        if(adapter == null) {
            adapter = getAdapter(this);
        }
        if(recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        if(recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setHasFixedSize(true);
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

    //endregion

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.Companion.getINTENT_EXTRA_MOVIE_ID(), movie.getId());
        startActivity(intentToStartDetailActivity);
    }

    //region Private

    private void fetchMovies() {
        LiveData<Movie[]> liveData = getMovieSource();
        liveData.observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (movies == null || movies.length == 0) {
                    layoutManager.setSpanCount(1); // 1 column needed for empty state layout
                } else {
                    layoutManager.setSpanCount(ViewUtils.calculateNumberOfColumns(context));
                }
                adapter.setMovies(movies);
            }
        });
    }
    //endregion

}