package com.accelo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.accelo.R
import com.accelo.data.interceptors.DynamicUrlInterceptor
import com.accelo.databinding.ActivityDeploymentBinding
import com.accelo.util.AcceloConstants
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class DeploymentActivity : AppCompatActivity() {


    lateinit var binding: ActivityDeploymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_deployment)

        binding.login.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.login.error = null
            }

        })

        binding.login.setOnClickListener {

            openAuthPage(binding.deployment.text.toString())
        }
    }

    private fun openAuthPage(deploymentName: String){

        if (deploymentName.isBlank()){
            binding.login.error = getString(R.string.empty_deployment_name)
            binding.login.requestFocus()
        }

        val intent = Intent(this, AuthActivity::class.java)

        intent.apply {
            putExtra(AcceloConstants.DEPLOYMENT_NAME, deploymentName)
        }
        startActivity(intent)
        finish()
    }
}