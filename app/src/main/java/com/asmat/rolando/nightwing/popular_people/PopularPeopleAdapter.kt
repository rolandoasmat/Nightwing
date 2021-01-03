package com.asmat.rolando.nightwing.popular_people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.ui.transformations.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_popular_person.view.*

class PopularPeopleAdapter: RecyclerView.Adapter<PopularPeopleAdapter.ViewHolder>() {

    private var data = listOf<PopularPersonUiModel>()

    fun setData(data: List<PopularPersonUiModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_popular_person, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.count()

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val personThumbnail = view.personThumbnail
        private val personName = view.personName
        private val personSubtitle = view.personSubtitle

        fun bind(data: PopularPersonUiModel) {
            data.posterURL?.let { url ->
                Picasso.get()
                        .load(url)
                        .resize(342, 513)
                        .centerCrop()
                        .transform(RoundedTransformation(50, 0))
                        .error(R.drawable.person)
                        .into(personThumbnail)
            } ?: run {
                val image = ResourcesCompat.getDrawable(view.resources, R.drawable.person, null)
                personThumbnail.setImageDrawable(image)
            }
            personName.text = data.name
            personSubtitle.text = data.subtitle
        }

    }


}