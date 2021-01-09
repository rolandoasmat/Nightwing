package com.asmat.rolando.nightwing.ui.row_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.extensions.invisible
import com.asmat.rolando.nightwing.extensions.visible
import com.asmat.rolando.nightwing.ui.common.BaseLinearAdapter
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_card_wide.view.*

private typealias MoviesViewHolder = MoviesLinearAdapter.ViewHolder

class MoviesLinearAdapter(
        callback: Callback<RowViewItemUiModel>,
        cardType: CardType = CardType.TALL) : BaseLinearAdapter<RowViewItemUiModel, MoviesViewHolder>(callback) {

    override val layoutForLinearItem = when (cardType) {
        CardType.TALL -> R.layout.item_row_card_tall
        CardType.WIDE -> R.layout.item_row_card_wide
    }


    override fun createViewHolder(view: View) = ViewHolder(view)

    inner class ViewHolder(val view: View) : BaseLinearAdapter<RowViewItemUiModel, MoviesViewHolder>.ViewHolder(view) {

        private val thumbnail: ImageView = view.itemRowCardImage
        private val title: TextView = view.itemRowCardImageCaption

        init {
            thumbnail.setOnClickListener(this)
        }

        override fun bind(item: RowViewItemUiModel) {
            if (item.url?.isEmpty() == true) {
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

    enum class CardType {
        TALL,
        WIDE
    }
}