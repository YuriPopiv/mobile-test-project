package com.accelo.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.accelo.ui.auth.DeploymentActivity
import com.accelo.ui.stream.StreamActivity
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class LauncherActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LauncherViewModel = viewModelProvider(viewModelFactory)

        viewModel.launchDestination.observe(this, EventObserver{
            if (it == Destination.AUTH_ACTIVITY){
                startActivity(Intent(this, DeploymentActivity::class.java))
            }else {
                startActivity(Intent(this, StreamActivity::class.java))
            }
            finish()
        })
    }
}