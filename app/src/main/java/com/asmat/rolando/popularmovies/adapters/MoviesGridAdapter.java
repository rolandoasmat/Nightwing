package com.asmat.rolando.popularmovies.adapters;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesGridAdapterViewHolder> {

    /**
     * Data set of Movie objects
     */
    ArrayList<Movie> mMovies;

    /**
     * Default constructor
     */
    public MoviesGridAdapter() {}

    /**
     * Set list of movies
     */
    public void setMovies() {

    }

    /**
     * Add any newly fetched movies from API
     */
    public void addMovies() {

    }

    /**
     * Sort
     */
    public void sortBy() {

    }

    @Override
    public MoviesGridAdapter.MoviesGridAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MoviesGridAdapter.MoviesGridAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MoviesGridAdapterViewHolder extends RecyclerView.ViewHolder {

        public MoviesGridAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
