package com.asmat.rolando.popularmovies.ui.adapters.linear

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asmat.rolando.popularmovies.ui.adapters.AdapterOnClickHandler


abstract class BaseLinearAdapter<T, V : BaseLinearAdapter<T, V>.ViewHolder>(private val clickHandler: AdapterOnClickHandler<T>? = null) : RecyclerView.Adapter<V>() {

    var data: List<T> = emptyList()
        set(data) {
            field = data
            notifyDataSetChanged()
        }

    abstract val layoutForLinearItem: Int
    abstract fun createViewHolder(view: View): V

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val context = parent.context
        val layoutForLinearItem = layoutForLinearItem
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutForLinearItem, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * View Holder
     */
    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View) {
            val position = adapterPosition
            val item = data[position]
            clickHandler?.onClick(item)
        }

        abstract fun bind(item: T)
    }

}