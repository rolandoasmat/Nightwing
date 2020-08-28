package com.asmat.rolando.nightwing.movie_details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.networking.models.VideosResponse
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.utilities.URLUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trailer_linear_item.view.*

private typealias Video = VideosResponse.Video
private typealias VideoViewHolder = TrailersLinearAdapter.ViewHolder

class TrailersLinearAdapter(clickHandler: Callback<Video>?): BaseLinearAdapter<Video, VideoViewHolder>(clickHandler) {

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
            Picasso.get()
                    .load(url)
                    .into(thumbnail)
            caption.text = item.name
        }
    }
}