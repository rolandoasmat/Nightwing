package com.asmat.rolando.nightwing.ui.popularmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMoviesGridAdapter
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.poster_grid_item.view.*

class PopularMoviesPagingDataAdapter(
    private val callback: BaseMoviesGridAdapter.Callback? = null,
    diffCallback: DiffUtil.ItemCallback<MovieGridItemUiModel>
) : PagingDataAdapter<MovieGridItemUiModel, PopularMoviesPagingDataAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.poster_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView? = itemView.poster
        private val label: TextView? = itemView.label

        init {
            poster?.setOnClickListener {
                getItem(bindingAdapterPosition)?.id?.let { id ->
                    callback?.itemPressed(id)
                }
            }
        }

        fun bind(uiModel: MovieGridItemUiModel?) {
            uiModel?.posterURL?.let { url ->
                Picasso.get()
                    .load(url)
                    .resize(340, 500)
                    .into(poster)
            } ?: run {
                val resources = itemView.resources
                val image = resources.getDrawable(R.drawable.ic_photo_default, null)
                poster?.setImageDrawable(image)
            }
            label?.text = uiModel?.title
        }
    }

    interface Callback {
        fun itemPressed(movieID: Int)
    }
}