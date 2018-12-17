package com.developers.team100k.rufus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.PurchasesUpdatedListener
import kotlinx.android.synthetic.main.activity_payment.*


/**
 * Created by Richard Hrmo
 * Activity handling payments and Billing Library
 * Need to register Developer Account
 */

class PaymentActivity : AppCompatActivity() {

    lateinit private var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        cheap.setOnClickListener {
            Toast.makeText(this, "Payment currently not available", Toast.LENGTH_SHORT).show();
        }

        expensive.setOnClickListener {
            Toast.makeText(this, "Payment currently not available", Toast.LENGTH_SHORT).show();
        }


//        billingClient = BillingClient.newBuilder(this).setListener(this).build()
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
//                if (billingResponseCode == BillingClient.BillingResponse.OK) {
//                    // The billing client is ready. You can query purchases here.
//                }
//            }
//            override fun onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        })

        back.setOnClickListener {
            finish()
        }


    }
}
