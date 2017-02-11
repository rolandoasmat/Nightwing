package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by rolandoasmat on 2/9/17.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesGridAdapterViewHolder> {

    /**
     * Data set of Movie objects
     */
    private ArrayList<Movie> mMovies;

    /**
     * Default constructor
     */
    public MoviesGridAdapter() {}

    // ----------------------------- API -----------------------------
    /**
     * Set list of movies
     */
    public void setMovies(Movie[] movies) {
        mMovies  = new ArrayList<>(Arrays.asList(movies));
        notifyDataSetChanged();
    }

    /**
     * Add any newly fetched movies from API
     */
    public void addMovies(Movie movie) {
        mMovies.add(movie);
        int position = mMovies.size();
        notifyItemInserted(position);
    }
    // ----------------------------------------------------------

    // ----------------------------- Overrides -----------------------------
    /**
     * Called whenever a ViewHolder is needed. Happens when the RecyclerView is layed out
     * Enough are created to fill the screen.
     *
     * @param parent ViewGroup where ViewHolders will be contained in.
     * @param viewType Used if different types of ViewHolders, not the case here.
     *
     * @return ViewHolder
     */
    @Override
    public MoviesGridAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        return new MoviesGridAdapterViewHolder(view);
    }

    /**
     * Called by RecyclerView to specify how to display data on the passed in position.
     *
     * @param holder ViewHolder to update with correct data, using given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesGridAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String posterURL = movie.getPosterURL();
        ImageView imageView = holder.mMoviePoster;
        Picasso.with(imageView.getContext()).load(posterURL).into(imageView);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return 0;
        } else {
            return mMovies.size();
        }
    }

    // ----------------------------------------------------------


    // ----------------------------- ViewHolder -----------------------------
    /**
     * Cache of the children views for a forecast list item.
     */
    class MoviesGridAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMoviePoster;

        /**
         * Creates cache of view holder
         *
         * @param itemView Layout of view holder
         */
        public MoviesGridAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_grid_item);
        }
    }
    // ----------------------------------------------------------
}
