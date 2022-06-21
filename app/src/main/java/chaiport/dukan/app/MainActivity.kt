package chaiport.dukan.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import chaiport.dukan.app.RequestBodies.getTokenizationRequest
import chaiport.dukan.app.RequestBodies.getWebCheckoutRequest
import chaiport.dukan.app.RequestBodies.getWithoutTokenizationRequest
import com.android.chaipaylibrary.ChaiPort
import com.android.chaipaylibrary.ChaiPortImpl
import com.android.chaipaylibrary.constants.PAYMENT_STATUS
import com.android.chaipaylibrary.constants.PAYMENT_STATUS_REQUEST_CODE
import com.android.chaipaylibrary.constants.PAYOUT_REQUEST_CODE
import com.android.chaipaylibrary.constants.RESULT_CODE
import com.android.chaipaylibrary.dto.PaymentDto
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chaiPort: ChaiPort

        chaiPort = ChaiPortImpl(
            context = this,
            environment = RequestBodies.environment,
            devEnvironment = RequestBodies.devEnvironment
        )

        if (null != intent && null != intent.data) {
            val paymentStatus = intent.data.toString()
            chaiPort.updatePaymentStatus(paymentStatus)
        }

        val paymentStatusObserver =
            Observer<PaymentDto.CheckoutUsingCreditCardResponse> { paymentStatus ->
                tvPaymentStatusValue.text = paymentStatus.toString()
            }

        chaiPort.showPaymentStatus().observe(this, paymentStatusObserver)

        buttonWebCheckout.setOnClickListener {
            chaiPort.checkoutUsingWeb(
                token = Utility.getJwtToken(),
                clientKey = RequestBodies.clientKey,
                request = getWebCheckoutRequest()
            )
        }
        buttonNonTokenization.setOnClickListener {
            chaiPort.checkoutWithoutTokenization(request = getWithoutTokenizationRequest())
        }
        buttonTokenization.setOnClickListener {
            chaiPort.initiatePaymentWithTokenization(request = getTokenizationRequest())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CODE && data != null) {
            when (requestCode) {
                PAYOUT_REQUEST_CODE, PAYMENT_STATUS_REQUEST_CODE -> {
                    val paymentStatus: PaymentDto.PaymentStatus? =
                        (data.getSerializableExtra(PAYMENT_STATUS)
                            ?: "Empty") as PaymentDto.PaymentStatus
                    val gson = Gson()
                    val json = gson.toJson(paymentStatus)
                    tvPaymentStatusValue.text = json
                }

            }

        }
    }
}