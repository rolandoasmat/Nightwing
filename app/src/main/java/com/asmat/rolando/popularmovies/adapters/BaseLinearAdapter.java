package com.asmat.rolando.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.asmat.rolando.popularmovies.models.AdapterOnClickHandler;

public abstract class BaseLinearAdapter<T, V extends BaseLinearAdapter.ViewHolder> extends RecyclerView.Adapter<V> {

    private T[] data;
    private final AdapterOnClickHandler<T> clickHandler;

    //region API

    BaseLinearAdapter() {
        this.clickHandler = null;
    }
    BaseLinearAdapter(AdapterOnClickHandler<T> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void setData(T[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public T[] getData() {
        return data;
    }

    //endregion

    //region Abstract
    abstract int getLayoutForLinearItem();
    abstract V createViewHolder(View view);
    //endregion

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForLinearItem = getLayoutForLinearItem();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForLinearItem, parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        T item = data[position];
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.length;
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(View view) {
            super(view);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            T item = data[position];
            if (clickHandler != null) {
                clickHandler.onClick(item);
            }
        }

        abstract void bind(T item);
    }

}