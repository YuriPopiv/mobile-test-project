package com.accelo.ui.stream

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Yuri Popiv on 12/6/2019.
 */
class StreamItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            val adapter = parent.adapter
            if (adapter != null) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                if (parent.getChildAdapterPosition(view) != adapter.itemCount - 1) {
                    bottom = spaceHeight
                }
            }
        }
    }

}