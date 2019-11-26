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
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class StreamActivity: AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: StreamViewModel = viewModelProvider(viewModelFactory)

        val binding: ActivityStreamBinding = DataBindingUtil.setContentView(this, R.layout.activity_stream)
        val adapter = StreamAdapter{item ->


        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        viewModel.getActivities()

        viewModel.activityData.observe(this, EventObserver{
            adapter.submitList(it.response!!.threads)
        })
    }
}