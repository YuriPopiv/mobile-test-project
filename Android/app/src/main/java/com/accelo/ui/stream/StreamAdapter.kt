package com.accelo.ui.stream

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.accelo.R
import com.accelo.data.model.Activity
import com.accelo.data.model.Owner
import com.accelo.data.model.Thread
import com.accelo.util.DateUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamAdapter(
    private val itemClickedListener: (Thread) -> Unit
) : ListAdapter<Thread, StreamAdapter.StreamViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.stream_list_item, parent, false)
        return StreamViewHolder(view, itemClickedListener)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class StreamViewHolder(
        private val view: View,
        private val itemClickedListener: (Thread) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val userImage: ImageView = view.findViewById(R.id.user_image)
        private val userName: TextView = view.findViewById(R.id.user_name)
        private val date: TextView = view.findViewById(R.id.date)
        private val title: TextView = view.findViewById(R.id.title)
        private val content: TextView = view.findViewById(R.id.content)
        private val interact: TextView = view.findViewById(R.id.interacts)

        @SuppressLint("SimpleDateFormat")
        fun bind(thread: Thread) {

            val activity = thread.activities?.get(0)
            val from = activity?.interacts?.first { owner -> owner.type == "from" }

            activity?.apply {
                userName.text = from?.ownerName
                date.text = DateUtil.getTimeFromTimeStamp(dateLogged!!.toLong())
                title.text = subject
                content.text = previewBody
                var interracters = ""
                interacts?.forEach { owner: Owner -> interracters += "${owner.ownerName}," }
                interact.text = interracters
            }

            view.setOnClickListener {
                itemClickedListener(thread)
            }

        }

    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Thread>() {
            override fun areItemsTheSame(oldItem: Thread, newItem: Thread): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Thread, newItem: Thread): Boolean {
                return oldItem.eventText == newItem.eventText
            }

        }
    }
}