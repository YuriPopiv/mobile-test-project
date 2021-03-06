package com.accelo.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.accelo.ui.stream.StreamActivity
import com.accelo.util.EventObserver
import com.accelo.util.viewModelProvider
import com.google.android.material.snackbar.Snackbar
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
            val intent = Intent(this, StreamActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        viewModel.snackbarMessage.observe(this, EventObserver{
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

}