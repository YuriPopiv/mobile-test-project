package com.accelo.ui.stream

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

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)

        val binding: ActivityStreamBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_stream)
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
                    viewModel.search(query)
                    return true
                }
            })
        }

        binding.createActivity.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading

            override fun loadMoreItems() {
                isLoading = true
                currentPage++

                viewModel.getActivities(currentPage)
                isLoading = false
            }
        })

        viewModel.getActivities(currentPage)

        viewModel.activityData.observe(this, EventObserver {
            if (!it.threads.isNullOrEmpty()) {
                adapter.removeLoadingFooter()
                adapter.addAll(it.threads!!)
                adapter.addLoadingFooter()
            } else {
                adapter.setLoadingItem(false)
            }
        })

        viewModel.snackbarMessage.observe(this, EventObserver {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun showConfidentialDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.confidential_activity))
        alertDialog.setMessage(getString(R.string.confidential_message))

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)) { dialogInterface, i ->
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    companion object{
        const val PAGE_START = 0
    }
}