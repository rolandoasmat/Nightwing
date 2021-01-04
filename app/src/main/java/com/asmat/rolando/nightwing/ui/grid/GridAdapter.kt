package com.asmat.rolando.nightwing.ui.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_grid_card.view.*

class GridAdapter(private val callback: Callback): RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private var data = emptyList<GridItemUiModel>()

    fun setData(data: List<GridItemUiModel>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_grid_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.count()

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val posterImageView = view.thumbnail
        private val titleLabel = view.titleLabel

        init {
            view.setOnClickListener {
                val id = data[adapterPosition].id
                callback.cardClicked(id)
            }
        }

        fun bind(data: GridItemUiModel) {
            data.posterUrl?.let { url ->
                Picasso.get()
                        .load(url)
                        .resize(342, 513)
                        .centerCrop()
                        .error(R.drawable.person)
                        .into(posterImageView)
            } ?: run {
                val image = ResourcesCompat.getDrawable(view.resources, R.drawable.person, null)
                posterImageView.setImageDrawable(image)
            }
            titleLabel.text = data.title
        }
    }

    interface Callback {
        fun cardClicked(id: Int)
    }
}