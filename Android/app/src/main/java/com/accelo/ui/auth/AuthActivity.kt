package com.accelo.ui.auth

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.content.ContextCompat
import com.accelo.BuildConfig
import com.accelo.R
import com.accelo.util.AcceloConstants
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by Yuri Popiv on 11/26/2019.
 */
class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deploymentName = intent?.extras?.getString(AcceloConstants.DEPLOYMENT_NAME, "")

        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName?,
                client: CustomTabsClient?
            ) {
                val params =
                    "response_type=code&client_id=${BuildConfig.CLIENT_ID}&scope=write(all)"
                val intent = CustomTabsIntent.Builder().apply {
                    setToolbarColor(ContextCompat.getColor(
                        this@AuthActivity, R.color.colorPrimary))
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
            }


        }

        val ok = CustomTabsClient
            .bindCustomTabsService(this, CHROME_PACKAGE, connection)
    }

}

const val SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService"
const val CHROME_PACKAGE = "com.android.chrome"