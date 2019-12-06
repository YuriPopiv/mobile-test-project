package com.accelo.ui.stream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.accelo.R
import com.accelo.data.base.BaseRecyclerAdapter
import com.accelo.data.model.Owner
import com.accelo.data.model.Thread
import com.accelo.util.DateUtil
import kotlinx.android.synthetic.main.stream_list_item.view.*

/**
 * Created by dmytro on 12/3/19
 */
class StreamAdapter(private val itemClickedListener: (Thread) -> Unit) :
    BaseRecyclerAdapter<Thread, RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var isLoadingAdded: Boolean = false

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Thread())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        if (getItems().isNotEmpty() && getItems().last().id == null)
            removeAt(getItems().size - 1)
    }

    override fun getItemCount(): Int {
        return getItems().size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == getItems().size - 1 && isLoadingAdded) {
            LOADING
        } else {
            ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            ITEM -> StreamAdapterViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.stream_list_item,
                    parent,
                    false
                )
            )
            else -> StreamAdapterLoadingViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.stream_item_loading,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as StreamAdapterViewHolder

                val activity = item.activities?.first()
                val from = activity?.interacts?.firstOrNull { owner -> owner.type == "from" }

                activity?.apply {
                    viewHolder.userName.text = from?.ownerName
                    viewHolder.date.text = DateUtil.getTimeFromTimeStamp(dateLogged!!.toLong())
                    viewHolder.title.text = subject
                    viewHolder.content.text = previewBody
                    var interacters = ""
                    interacts?.forEach { owner: Owner -> interacters += "${owner.ownerName}," }
                    viewHolder.interact.text = interacters

                    viewHolder.streamItemLayout.setOnClickListener {
                        itemClickedListener(item)
                    }
                }
            }
            else -> {
                //LOADING
            }
        }
    }

    inner class StreamAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.user_image
        val userName = itemView.user_name
        val date = itemView.date
        val title = itemView.title
        val content = itemView.content
        val interact = itemView.interacts
        val streamItemLayout = itemView.stream_item_layout

    }

    inner class StreamAdapterLoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }
}