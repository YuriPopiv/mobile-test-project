package com.accelo.ui.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.accelo.R
import com.accelo.databinding.ActivityCreateBinding
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import com.accelo.workers.DeliveryWorker
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/28/2019.
 */
class CreateActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: ActivityCreateBinding

    lateinit var viewModel: CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create)
        viewModel = viewModelProvider(viewModelFactory)

        binding.lifecycleOwner = this@CreateActivity
        binding.viewModel = this@CreateActivity.viewModel

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.snackbarMessage.observe(this, EventObserver { reason ->
            Snackbar.make(binding.root, reason, Snackbar.LENGTH_SHORT).show()
        })

        viewModel.navigateToNetworkErrorDialogAction.observe(this, EventObserver {
            showNoNetworkErrorDialog()
        })

        //data may not update in the StreamActivity because there is delay on the server after activity is created
        viewModel.activityData.observe(this, EventObserver {
            Toast.makeText(this@CreateActivity, "Activity Created", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK, Intent())
            finish()
        })

        binding.subject.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.subject.error = null
            }

        })

    }

    private fun showNoNetworkErrorDialog() {
        val builder = AlertDialog.Builder(this)
        //builder.setTitle("Error")
        builder.setMessage("No Internet connection, unable to submit the activity.\nDo you want to retry now?")
        builder.setNegativeButton("Cancel", null)
        builder.setNeutralButton("Submit Later") { _, _ ->
            scheduleDelivery()
            viewModel.saveNotDeliveredActivitiesToDB(
                binding.subject.text.toString(),
                binding.body.text.toString()
            )
            finish()
        }
        builder.setPositiveButton("Retry Now") { _, _ ->
            createActivityAttempt()
        }
        builder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send -> {
                createActivityAttempt()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun scheduleDelivery(){

        //Of this is fist failure, schedule delivery when network will be available,
        // otherwise we assume that worker is already enqueued
        if (!viewModel.hasNotDeliveredActivities()) {

            //Schedule re-attemt when network will be available
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val request =
                OneTimeWorkRequestBuilder<DeliveryWorker>().setConstraints(constraints).build()
            WorkManager.getInstance(this).enqueue(request)
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
                .observe(ProcessLifecycleOwner.get(), Observer {
                    Timber.e("Worker status $it")
                })
        }
    }

    private fun createActivityAttempt() {
        val subject = binding.subject.text.toString()
        val body = binding.body.text.toString()

        if (subject.isBlank()) {
            binding.subject.error = "This field is required"
            binding.subject.requestFocus()
        } else {
            viewModel.createActivity(subject, body)
        }
    }


}