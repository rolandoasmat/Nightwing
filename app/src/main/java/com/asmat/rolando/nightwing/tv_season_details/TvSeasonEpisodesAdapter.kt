package com.asmat.rolando.nightwing.tv_season_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.invisible
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_tv_episode.view.*

class TvSeasonEpisodesAdapter : RecyclerView.Adapter<TvSeasonEpisodesAdapter.ViewHolder>() {

    private val episodes: List<TvSeasonEpisodesUiModel.Item>
        get() = data?.episodes ?: emptyList()

    var data: TvSeasonEpisodesUiModel? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutIdForGridItem = R.layout.item_row_tv_episode
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForGridItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(episodes[position])

    override fun getItemCount() = episodes.count()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvEpisodePoster: ImageView? = itemView.tvEpisodePoster
        private val episodeTitle: TextView? = itemView.episodeTitle
        private val episodeOverview: TextView? = itemView.episodeOverview

        fun bind(uiData: TvSeasonEpisodesUiModel.Item) {
            if (uiData.backdropURL?.isEmpty() == true) {
                tvEpisodePoster?.invisible()
            } else {
                tvEpisodePoster?.visible()
                Picasso.get()
                    .load(uiData.backdropURL)
                    .transform(RoundedTransformation(50, 0))
                    .into(tvEpisodePoster)
            }
            episodeTitle?.text = uiData.title
            episodeOverview?.text = uiData.body
        }
    }
}