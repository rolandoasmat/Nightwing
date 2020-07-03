package com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.popularmovies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.person_movie_credit_cell.view.*

private typealias MovieCredits = List<MovieCreditUiModel>

class MovieCreditsAdapter(private val callback: ItemCallback): RecyclerView.Adapter<MovieCreditsAdapter.ViewHolder>() {

    private var data: MovieCredits = emptyList()

    fun updateData(data: MovieCredits) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutIdForGridItem = R.layout.person_movie_credit_cell
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForGridItem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView? = itemView.posterImage
        private val characterName: TextView? = itemView.characterNameText
        private val movieTitleLabel: TextView? = itemView.movieTitleLabel

        fun bind(model: MovieCreditUiModel) {
            Picasso.get()
                    .load(model.posterURL)
                    .into(poster)
            val name = model.characterName ?: itemView.resources.getString(R.string.unknown_character)
            characterName?.text = name
            movieTitleLabel?.text = model.movieTitle
            poster?.setOnClickListener {
                callback.onMovieBannerClicked(model.id)
            }
        }
    }

    interface ItemCallback {
        fun onMovieBannerClicked(movieID: Int)
    }
}