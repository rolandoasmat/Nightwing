package com.asmat.rolando.popularmovies.ui.adapters.linear

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asmat.rolando.popularmovies.ui.adapters.AdapterOnClickHandler

abstract class BaseLinearAdapter<T, V : BaseLinearAdapter.ViewHolder> : RecyclerView.Adapter<V> {

    var data: List<T>? = null
        set(data) {
            field = data
            notifyDataSetChanged()
        }
    private val clickHandler: AdapterOnClickHandler<T>?

    //endregion

    //region Abstract
    internal abstract val layoutForLinearItem: Int

    //region API

    internal constructor() {
        this.clickHandler = null
    }

    internal constructor(clickHandler: AdapterOnClickHandler<T>) {
        this.clickHandler = clickHandler
    }

    internal abstract fun createViewHolder(view: View): V
    //endregion

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val context = parent.context
        val layoutForLinearItem = layoutForLinearItem
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutForLinearItem, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        val item = this.data!![position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if (this.data == null) 0 else this.data!!.size
    }

    internal abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View) {
            val position = adapterPosition
            val item = this.data!!.get(position)
            clickHandler?.onClick(item)
        }

        internal abstract fun bind(item: T)
    }

}