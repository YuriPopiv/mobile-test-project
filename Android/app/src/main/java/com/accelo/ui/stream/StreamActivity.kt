package com.accelo.ui.stream

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.accelo.R
import com.accelo.databinding.ActivityStreamBinding
import com.accelo.ui.create.CreateActivity
import com.accelo.ui.view.ViewActivity
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: StreamViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)

        val binding: ActivityStreamBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_stream)
        binding.lifecycleOwner = this@StreamActivity
        binding.viewModel = this@StreamActivity.viewModel

        val adapter = StreamAdapter { item ->
            Timber.d("Activity Id: ${item.activities?.get(0)}")

            startActivity(item.activities?.first()?.id?.let {
                ViewActivity.launchActivity(this@StreamActivity,
                    it
                )
            })
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
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        viewModel.getActivities()

        viewModel.activityData.observe(this, EventObserver {
            adapter.submitList(it.threads)
        })

        viewModel.snackbarMessage.observe(this, EventObserver {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    private fun dismissKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}