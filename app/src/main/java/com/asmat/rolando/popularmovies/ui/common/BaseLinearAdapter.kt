package com.asmat.rolando.popularmovies.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseLinearAdapter<T, V : BaseLinearAdapter<T, V>.ViewHolder>(private val clickHandler: ((T) -> Unit?)? = null) : androidx.recyclerview.widget.RecyclerView.Adapter<V>() {

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
    abstract inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View) {
            val position = adapterPosition
            val item = data[position]
            clickHandler?.invoke(item)
        }

        abstract fun bind(item: T)
    }

}