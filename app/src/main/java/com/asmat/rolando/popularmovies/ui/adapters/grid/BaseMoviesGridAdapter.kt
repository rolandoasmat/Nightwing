package com.asmat.rolando.popularmovies.ui.adapters.grid

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.adapters.MovieGridItemType
import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_grid_item.view.*

private typealias Movie = MoviesResponse.Movie
private typealias Movies = MutableList<Movie>

abstract class BaseMoviesGridAdapter(private val clickHandler: MovieAdapterOnClickHandler) : RecyclerView.Adapter<BaseMoviesGridAdapter.ViewHolder>() {

    private var showEmptyState = false
    private var movies: Movies = mutableListOf()

    abstract val emptyStateLayoutID: Int

    fun setMovies(movies: List<Movie>) {
        this.movies.clear()
        addMovies(movies)
    }

    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        showEmptyState = movies.isEmpty()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (showEmptyState) {
            MovieGridItemType.EMPTY.ordinal
        } else {
            MovieGridItemType.REGULAR.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutIdForGridItem = when (viewType) {
            MovieGridItemType.EMPTY.ordinal -> emptyStateLayoutID
            MovieGridItemType.REGULAR.ordinal ->  R.layout.movie_grid_item
            else -> R.layout.movie_grid_item
        }
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForGridItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!showEmptyState) {
            val movie = movies[position]
            holder.bind(movie)
        }
    }

    override fun getItemCount(): Int {
        return if (showEmptyState) {
            1
        } else {
            movies.size
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val poster: ImageView? = itemView.poster

        fun bind(movie: Movie) {
            movie.poster_path?.let { posterPath ->
                val posterURL = URLUtils.getImageURL342(posterPath)
                Picasso.with(poster?.context)
                        .load(posterURL)
                        .resize(340, 500)
                        .into(poster)
            }

            poster?.setOnClickListener {
                onClick(poster)
            }
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            val movie = movies[position]
            clickHandler.onClick(movie)
        }
    }

}