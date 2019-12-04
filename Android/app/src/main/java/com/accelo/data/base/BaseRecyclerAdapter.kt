package com.accelo.data.base

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by dmytro on 12/3/19
 */

abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var items: MutableList<T> = ArrayList()

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): T = items[position]

    fun getItems(): List<T> = items

    fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(items: List<T>) {
        this.items.addAll(items)
        notifyItemRangeInserted(itemCount - items.size, items.size)
    }

    fun insertAt(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun insertAll(position: Int, items: List<T>) {
        this.items.addAll(position, items)
        notifyItemRangeInserted(position, items.size)
    }

    fun replaceAt(position: Int, item: T) {
        items[position] = item
        notifyItemChanged(position)
    }

    fun replaceAll(items: List<T>) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val position = items.indexOf(item)
        if (items.remove(item)) {
            notifyItemRemoved(position)
        }
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}