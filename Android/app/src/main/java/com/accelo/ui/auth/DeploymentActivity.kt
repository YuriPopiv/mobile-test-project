package com.accelo.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.accelo.R
import com.accelo.databinding.ActivityDeploymentBinding
import com.accelo.util.AcceloConstants

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class DeploymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDeploymentBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_deployment)
        binding.login.setOnClickListener {

            openAuthPage(binding.editText.text.toString())
        }
    }

    private fun openAuthPage(deploymentName: String){

        val intent = Intent(this, AuthActivity::class.java)

        intent.apply {
            putExtra(AcceloConstants.DEPLOYMENT_NAME, deploymentName)
        }
        startActivity(intent)
    }
}