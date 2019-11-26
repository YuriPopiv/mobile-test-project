package com.accelo.ui.auth

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class TokenActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Uri? = intent?.data
        val viewModel: TokenViewModel = viewModelProvider(factory)

        val code = data?.getQueryParameter("code")
        viewModel.getToken(code!!)

        viewModel.userResponse.observe(this, EventObserver{
            //startActivity(Intent(this, StreamActivity::class.java))
        })
    }

}