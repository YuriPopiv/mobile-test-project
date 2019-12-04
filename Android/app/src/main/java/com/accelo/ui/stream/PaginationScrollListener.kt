package com.accelo.ui.stream

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by dmytro on 12/3/19
 */
abstract class PaginationScrollListener(
    private var layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {
    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    protected abstract fun loadMoreItems()
}