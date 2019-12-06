package com.accelo.ui.stream

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.accelo.R
import com.accelo.databinding.ActivityStreamBinding
import com.accelo.ui.create.CreateActivity
import com.accelo.ui.view.ViewActivity
import com.accelo.util.EventObserver
import com.accelo.util.KeyboardUtil.dismissKeyboard
import com.accelo.util.viewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: StreamViewModel

    lateinit var  binding: ActivityStreamBinding

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream)
        binding.lifecycleOwner = this@StreamActivity
        binding.viewModel = this@StreamActivity.viewModel

        val adapter = StreamAdapter { item ->
            val activity = item.activities?.first()

            activity?.let {
                if (it.confidential == 1) {
                    showConfidentialDialog()
                } else {
                    startActivity(ViewActivity.launchActivity(this@StreamActivity, it.id!!))
                }
            }
        }

        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    dismissKeyboard(this@apply)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    if(query.isNotEmpty()) {
                        viewModel.search(query)
                    } else {
                        viewModel.refresh()
                    }
                    return true
                }
            })
        }

        binding.createActivity.setOnClickListener {
            startActivityForResult(Intent(this, CreateActivity::class.java), ACTIVITY_REQUEST_CODE)
        }

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(StreamItemDecoration(resources.getDimension(R.dimen.item_decoration_padding).toInt()))
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.getActivities(currentPage, getSearchQuery())
            }
        })

        binding.refresh.setOnRefreshListener {
            viewModel.refresh(getSearchQuery())
        }

        viewModel.getActivities(currentPage)

        viewModel.activityData.observe(this, EventObserver {

            if (!it.threads.isNullOrEmpty()) {
                adapter.removeLoadingFooter()
                adapter.addAll(it.threads!!)
                adapter.addLoadingFooter()
                isLoading = false
            } else {
                isLastPage = true

                adapter.removeLoadingFooter()
            }
        })

        viewModel.refreshData.observe(this, EventObserver{
            if (!it.threads.isNullOrEmpty()) {
                adapter.replaceAll(it.threads!!)
                adapter.addLoadingFooter()
                isLoading = false
                isLastPage = false
                currentPage = PAGE_START
            }
        })

        viewModel.searchData.observe(this, EventObserver{
            if (!it.threads.isNullOrEmpty()) {
                adapter.replaceAll(it.threads!!)
                adapter.removeLoadingFooter()
                isLoading = false
                isLastPage = false
                currentPage = PAGE_START
            }
        })

        viewModel.snackbarMessage.observe(this, EventObserver {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_REQUEST_CODE) {
            viewModel.refresh()
        }
    }

    private fun getSearchQuery(): String? = binding.searchView.query.toString().ifBlank { null }

    private fun showConfidentialDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.confidential_activity))
        alertDialog.setMessage(getString(R.string.confidential_message))

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.ok)
        ) { _, _ ->
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}

const val PAGE_START = 0
const val ACTIVITY_REQUEST_CODE = 1