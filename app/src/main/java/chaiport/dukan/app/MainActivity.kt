package chaiport.dukan.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import chaiport.dukan.app.RequestBodies.getTokenizationRequest
import chaiport.dukan.app.RequestBodies.getWebCheckoutRequest
import chaiport.dukan.app.RequestBodies.getWithoutTokenizationRequest
import com.android.chaipaylibrary.PortOne
import com.android.chaipaylibrary.PortOneImpl
import com.android.chaipaylibrary.constants.PAYMENT_STATUS
import com.android.chaipaylibrary.constants.PAYMENT_STATUS_REQUEST_CODE
import com.android.chaipaylibrary.constants.PAYOUT_REQUEST_CODE
import com.android.chaipaylibrary.constants.RESULT_CODE
import com.android.chaipaylibrary.dto.PaymentDto
import com.android.chaipaylibrary.networkCalls.ApiCallbackInterface
import com.android.chaipaylibrary.utils.LoggerUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val portone: PortOne

        portone = PortOneImpl(
            context = this,
            environment = RequestBodies.environment,
            devEnvironment = RequestBodies.devEnvironment
        )

        if (null != intent && null != intent.data) {
            val paymentStatus = intent.data.toString()
            portone.updatePaymentStatus(paymentStatus)
        }

        portone.getBankList(
            "GBPRIMEPAY",
            RequestBodies.getBankListRequest(),
            object : ApiCallbackInterface<PaymentDto.BankListResponse> {
                override fun onSucceed(response: PaymentDto.BankListResponse) {
                    LoggerUtil.info("Successful")
                }

                override fun onFailure(
                    errorCode: Int?,
                    status: String?,
                    errorMessage: String,
                    throwable: Throwable
                ) {
                    LoggerUtil.info("Failed")
                }

            })

        portone.getDBTDetails(RequestBodies.clientKey,
            object : ApiCallbackInterface<PaymentDto.DBTResponse> {
                override fun onSucceed(response: PaymentDto.DBTResponse) {
                    LoggerUtil.info("Successful")
                }

                override fun onFailure(
                    errorCode: Int?,
                    status: String?,
                    errorMessage: String,
                    throwable: Throwable
                ) {
                    LoggerUtil.info("Failed")
                }

            })

        buttonWebCheckout.setOnClickListener {
            val gson = Gson()
            val json = gson.toJson(getWebCheckoutRequest())
            Log.i("WebCheckoutRequest", json)
            val token = Utility.getJwtToken()
            portone.checkoutUsingWeb(
                token = token,
                portoneKey = RequestBodies.clientKey,
                request = getWebCheckoutRequest()
            )
        }
        buttonNonTokenization.setOnClickListener {
            portone.checkoutWithoutTokenization(request = getWithoutTokenizationRequest())
        }
        buttonTokenization.setOnClickListener {
            portone.checkoutWithTokenization(request = getTokenizationRequest())
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