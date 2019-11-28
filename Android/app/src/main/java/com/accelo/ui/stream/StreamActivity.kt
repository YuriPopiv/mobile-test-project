package com.accelo.ui.stream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.accelo.R
import com.accelo.databinding.ActivityStreamBinding
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: StreamViewModel = viewModelProvider(viewModelFactory)

        val binding: ActivityStreamBinding = DataBindingUtil.setContentView(this, R.layout.activity_stream)
        val adapter = StreamAdapter{item ->

            Timber.d("Activity Id: ${item.activities?.get(0)}")

        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        viewModel.getActivities()

        viewModel.activityData.observe(this, EventObserver{
            adapter.submitList(it.response!!.threads)
        })

        viewModel.snackbarMessage.observe(this, EventObserver{
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }
}