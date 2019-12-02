package com.accelo.ui.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.accelo.R
import com.accelo.data.model.FullActivity
import com.accelo.databinding.ActivityViewBinding
import com.accelo.util.DateUtil.getTimeFromTimeStamp
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by dmytro on 12/2/19
 */
class ViewActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: ViewActivityViewModel
    lateinit var binding: ActivityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view)

        viewModel = viewModelProvider(factory)

        binding.lifecycleOwner = this@ViewActivity
        binding.viewModel = this@ViewActivity.viewModel

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.getFullActivity(intent.getStringExtra(ACTIVITY_ID)!!)

        viewModel.fullActivityResponse.observe(this, EventObserver {
            setActivityInfo(it)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setActivityInfo(fullActivityResponse: FullActivity) {
        binding.userName.text = fullActivityResponse.interacts.first { it.type == "from" }.ownerName
        binding.dateLogged.text = getTimeFromTimeStamp(fullActivityResponse.dateLogged.toLong())
        binding.subject.text =
            String.format("%s %s", getString(R.string.subject), fullActivityResponse.subject)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.body.text =
                Html.fromHtml(fullActivityResponse.htmlBody, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            binding.body.text = Html.fromHtml(fullActivityResponse.htmlBody)
        }
    }

    companion object {
        const val ACTIVITY_ID = "activityId"
        fun launchActivity(context: Context, activityId: String): Intent {
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra(ACTIVITY_ID, activityId)

            return intent
        }
    }
}