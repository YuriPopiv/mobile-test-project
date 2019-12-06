package com.accelo.ui.auth

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.accelo.BuildConfig
import com.accelo.R
import com.accelo.util.AcceloConstants
import com.accelo.util.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AuthActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deploymentName = intent?.extras?.getString(AcceloConstants.DEPLOYMENT_NAME, "")
        val viewModel: AuthViewModel = viewModelProvider(factory)

        if (deploymentName != null) {

            viewModel.saveDeploymentName(deploymentName)

            val connection = object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(
                    name: ComponentName?,
                    client: CustomTabsClient?
                ) {
                    val params =
                        "response_type=code&client_id=${BuildConfig.CLIENT_ID}&scope=write(all)"
                    val intent = CustomTabsIntent.Builder().apply {
                        setToolbarColor(
                            ContextCompat.getColor(
                                this@AuthActivity, R.color.colorPrimary
                            )
                        )
                        setSecondaryToolbarColor(
                            ContextCompat.getColor(
                                this@AuthActivity,
                                R.color.colorPrimaryDark
                            )
                        )
                    }.build()
                    intent.intent.setPackage(CHROME_PACKAGE)
                    intent.launchUrl(
                        this@AuthActivity,
                        Uri.parse(
                            "https://$deploymentName.api.accelo.com/oauth2/v0/authorize?" +
                                    params
                        )
                    )
                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    Timber.e("Chrome service disconnected")
                }


            }

            val ok = CustomTabsClient
                .bindCustomTabsService(this, CHROME_PACKAGE, connection)
        }else{
            //TODO handle error
        }
    }

}

const val SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService"
const val CHROME_PACKAGE = "com.android.chrome"