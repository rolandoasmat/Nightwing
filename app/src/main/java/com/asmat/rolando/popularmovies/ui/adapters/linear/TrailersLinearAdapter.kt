package com.asmat.rolando.popularmovies.ui.adapters.linear

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trailer_linear_item.view.*

private typealias Video = VideosResponse.Video
private typealias VideoViewHolder = TrailersLinearAdapter.ViewHolder

class TrailersLinearAdapter(clickHandler: (Video) -> Unit?) :
        BaseLinearAdapter<Video, VideoViewHolder>(clickHandler) {

    override val layoutForLinearItem: Int
        get() = R.layout.trailer_linear_item


    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : BaseLinearAdapter<Video, VideoViewHolder>.ViewHolder(view) {
        private val thumbnail: ImageView = view.iv_trailer_thumbnail
        private val caption: TextView = view.tv_trailer_caption

        init {
            thumbnail.setOnClickListener(this)
        }

        override fun bind(item: Video) {
            val url = URLUtils.getYoutubeThumbnailURL(item.key)
            Picasso.with(thumbnail.context)
                    .load(url)
                    .into(thumbnail)
            caption.text = item.name
        }
    }
}