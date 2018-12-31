package com.asmat.rolando.popularmovies.ui.adapters.linear

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.ui.transformations.RoundedTransformation
import com.asmat.rolando.popularmovies.utilities.URLUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cast_linear_item.view.*

private typealias Cast = CreditsResponse.Cast
private typealias CastViewHolder = CastLinearAdapter.ViewHolder

class CastLinearAdapter(clickHandler: (Cast) -> Unit?) :
        BaseLinearAdapter<Cast, CastViewHolder>(clickHandler) {

    override val layoutForLinearItem: Int
        get() = R.layout.cast_linear_item

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : BaseLinearAdapter<Cast, CastViewHolder>.ViewHolder(view) {

        private val thumbnail: ImageView = view.thumbnail
        private val name: TextView = view.name
        private val role: TextView = view.role

        init {
            thumbnail.setOnClickListener(this)
        }

        override fun bind(item: Cast) {
            name.text = item.name
            role.text = item.character
            item.profile_path?.let { profilePath ->
                val url = URLUtils.getImageURL342(profilePath)
                Picasso.with(thumbnail.context)
                        .load(url)
                        .resize(342, 513)
                        .centerCrop()
                        .transform(RoundedTransformation(50, 0))
                        .error(R.drawable.person)
                        .into(thumbnail)
            }
        }
    }
}
