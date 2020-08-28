package com.asmat.rolando.nightwing.movie_details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.invisible
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_card.view.*

private typealias MoviesViewHolder = MoviesLinearAdapter.ViewHolder

class MoviesLinearAdapter(callback: Callback<MovieCardUIModel>): BaseLinearAdapter<MovieCardUIModel, MoviesViewHolder>(callback) {

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