package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        this.mClickHandler = handler;
    }

    public void setMovies(Movie[] movies) {
        this.setMovies(Arrays.asList(movies));
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies  = movies;
        notifyDataSetChanged();
    }

    public void addMovies(ArrayList<Movie> movies) {
        int indexOfFirstNewItem = this.mMovies.size();
        mMovies.addAll(movies);
        notifyItemRangeInserted(indexOfFirstNewItem,indexOfFirstNewItem+movies.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return 0;
        } else {
            return mMovies.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMoviePoster;

        ViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.iv_movie_grid_item);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            String posterURL = movie.getPosterURL();
            Picasso.with(mMoviePoster.getContext())
                    .load(posterURL)
                    .resize(340, 500)
                    .into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovies.get(position);
            mClickHandler.onClick(movie);
        }
    }
}