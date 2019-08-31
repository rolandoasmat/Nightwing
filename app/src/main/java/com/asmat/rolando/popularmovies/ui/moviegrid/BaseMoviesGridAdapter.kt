package com.asmat.rolando.popularmovies.ui.moviegrid

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.popularmovies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_grid_item.view.*

private typealias Movies = List<MovieGridItemUiModel>

class BaseMoviesGridAdapter(val callback: Callback? = null) : RecyclerView.Adapter<BaseMoviesGridAdapter.ViewHolder>() {

    private var movies: Movies = emptyList()

    fun setMovies(movies: Movies) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.movie_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        movies.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView? = itemView.poster
        private val label: TextView? = itemView.label

        fun bind(uiModel: MovieGridItemUiModel) {
            uiModel.posterURL?.let { url ->
                Picasso.get()
                        .load(url)
                        .resize(340, 500)
                        .into(poster)
            } ?: run {
                val resources = itemView.resources
                val image = resources.getDrawable(R.drawable.ic_photo_default, null)
                poster?.setImageDrawable(image)
            }
            label?.text = uiModel.title
            poster?.setOnClickListener {
                callback?.itemPressed(adapterPosition)
            }
        }
    }

    interface Callback {
        fun itemPressed(index: Int)
    }

}