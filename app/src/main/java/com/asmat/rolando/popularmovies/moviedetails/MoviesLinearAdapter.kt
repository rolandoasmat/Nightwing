package com.asmat.rolando.popularmovies.moviedetails

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.invisible
import com.asmat.rolando.popularmovies.extensions.visible
import com.asmat.rolando.popularmovies.ui.common.BaseLinearAdapter
import com.asmat.rolando.popularmovies.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_card.view.*

private typealias MoviesViewHolder = MoviesLinearAdapter.ViewHolder

class MoviesLinearAdapter(clickHandler: (MovieCardUIModel)->Unit?): BaseLinearAdapter<MovieCardUIModel, MoviesViewHolder>() {

    override val layoutForLinearItem = R.layout.item_movie_card

    override fun createViewHolder(view: View) = ViewHolder(view)

    inner class ViewHolder(val view: View): BaseLinearAdapter<MovieCardUIModel, MoviesViewHolder>.ViewHolder(view) {

        private val thumbnail: ImageView = view.thumbnail
        private val title: TextView = view.title

        init {
            thumbnail.setOnClickListener(this)
        }

        override fun bind(item: MovieCardUIModel) {
            if (item.url.isEmpty()) {
                thumbnail.invisible()
            } else {
                thumbnail.visible()
                Picasso.get()
                        .load(item.url)
                        .resize(342, 513)
                        .centerCrop()
                        .transform(RoundedTransformation(50, 0))
                        .error(R.drawable.person)
                        .into(thumbnail)
            }
            title.text = item.title
        }
    }
}