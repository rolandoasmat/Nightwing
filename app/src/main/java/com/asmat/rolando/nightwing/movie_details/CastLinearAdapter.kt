package com.asmat.rolando.nightwing.movie_details

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.networking.models.CreditsResponse
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.asmat.rolando.nightwing.utilities.URLUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cast_linear_item.view.*

typealias Cast = CreditsResponse.Cast
private typealias CastViewHolder = CastLinearAdapter.ViewHolder

class CastLinearAdapter(clickHandler: Callback<Cast>): BaseLinearAdapter<Cast, CastViewHolder>(clickHandler) {

    override val layoutForLinearItem: Int
        get() = R.layout.cast_linear_item

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(val view: View): BaseLinearAdapter<Cast, CastViewHolder>.ViewHolder(view) {

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
                Picasso.get()
                        .load(url)
                        .resize(342, 513)
                        .centerCrop()
                        .transform(RoundedTransformation(50, 0))
                        .error(R.drawable.person)
                        .into(thumbnail)
            } ?: run {
                val resources = view.resources
                val image = resources.getDrawable(R.drawable.person, null)
                thumbnail.setImageDrawable(image)
            }
        }
    }
}
