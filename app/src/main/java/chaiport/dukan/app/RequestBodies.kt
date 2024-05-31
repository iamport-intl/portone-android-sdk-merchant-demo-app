package chaiport.dukan.app

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.chaipaylibrary.constants.*
import com.android.chaipaylibrary.dto.CheckoutPaymentDto
import com.android.chaipaylibrary.dto.PaymentDto
import java.time.Instant
import java.time.format.DateTimeFormatter

object RequestBodies {

    var paymentChannel = "MOMOPAY"
    var paymentMethod = "MOMOPAY_WALLET"
    var devEnvironment = DEV
    var clientKey = CLIENT_KEY_Dev3
    var secretKey = SECRET_KEY_Dev3
    var currency = VND
    var phoneNo2 = "+919913379694"
    var phoneNo = "+848959893980"
    var environment = SANDBOX
    var orderList = ArrayList<CheckoutPaymentDto.OrderDetail>()

    val AdyenCreditCard = PaymentDto.NewCard(
        cardNumber = "4111111145551142",
        cardType = "Visa",
        cardholderName = "NGUYEN VAN A",
        serviceCode = "737",
        expiryYear = "2030",
        expiryMonth = "03"
    )

    val OmiseCreditCard = PaymentDto.NewCard(
        cardNumber = "4000000000000002",
        cardType = "Visa",
        cardholderName = "NGUYEN VAN A",
        serviceCode = "737",
        expiryYear = "2022",
        expiryMonth = "11"
    )

    val NinePayCreditCard = PaymentDto.NewCard(
        cardNumber = "4005555555000009",
        cardType = "Visa",
        cardholderName = "NGUYEN VAN A",
        serviceCode = "123",
        expiryYear = "2025",
        expiryMonth = "05"
    )

    val VTCPayCreditCard = PaymentDto.NewCard(
        cardNumber = "4111111111111111",
        cardType = "Visa",
        cardholderName = "NGUYEN VAN A",
        serviceCode = "123",
        expiryYear = "2030",
        expiryMonth = "01"
    )

    val FoxPayCreditCard = PaymentDto.NewCard(
        cardNumber = "4000000000000002",
        cardType = "Visa",
        cardholderName = "NGUYEN VAN A",
        serviceCode = "111",
        expiryYear = "2022",
        expiryMonth = "12"
    )

    val ExistingCardDetails = PaymentDto.CardDetails(
        token = "9452f6170b0c43389a2fadb9a3a55eda",
        partialCardNumber = "222300******0023",
        expiryMonth = "05",
        expiryYear = "2021",
        type = "mastercard"
    )

    fun getWebCheckoutRequest(): CheckoutPaymentDto.CheckoutUsingWebRequest {

        val orderId = Utility.getRandomString(10)
        val signatureHash =
            Utility.getSignatureHash(
                "50010",
                currency,
                "https://dev-checkout.portone.io/failure.html",
                orderId,
                clientKey,
                "https://dev-checkout.portone.io/success.html"
            )

        val orderDetail: CheckoutPaymentDto.OrderDetail = CheckoutPaymentDto.OrderDetail(
            id = "knb",
            name = "kim nguyen bao",
            price = 50010.00,
            quantity = 1,
            image = null
        )

        val orderList = ArrayList<CheckoutPaymentDto.OrderDetail>()
        orderList.add(orderDetail)

        val orderDetails = CheckoutPaymentDto.CheckoutUsingWebRequest()
        orderDetails.portoneKey = clientKey
        orderDetails.merchantDetails = CheckoutPaymentDto.MerchantDetails(
            name = "Gumnam",
            logo = null,
            backUrl = "https://demo.portone.io/checkout.html",
            promoCode = "Gumnam420",
            promoDiscount = 10000,
            shippingCharges = 10000.00
        )

        orderDetails.merchantOrderId = orderId
        orderDetails.signatureHash = signatureHash.trim()
        orderDetails.amount = 50010.00
        orderDetails.currency = currency
        orderDetails.countryCode = "VN"
        orderDetails.billingDetails = CheckoutPaymentDto.BillingDetails(
            billingName = "Test mark",
            billingEmail = "markweins@gmail.com",
            billingPhone = phoneNo,
            billingAddress = CheckoutPaymentDto.Address(
                city = currency, countryCode = "VN", locale = "en",
                line_1 = "address",
                line_2 = "address_2",
                postal_code = "400202",
                state = "Mah"
            )
        )
        orderDetails.shippingDetails = CheckoutPaymentDto.ShippingDetails(
            shippingName = "Test mark",
            shippingEmail = "markweins@gmail.com",
            shippingPhone = phoneNo,
            shippingAddress = CheckoutPaymentDto.Address(
                city = currency, countryCode = "VN", locale = "en",
                line_1 = "address",
                line_2 = "address_2",
                postal_code = "400202",
                state = "Mah"
            )
        )
        orderDetails.orderDetails = orderList
        orderDetails.successUrl = "https://dev-checkout.portone.io/success.html"
        orderDetails.failureUrl = "https://dev-checkout.portone.io/failure.html"
        orderDetails.expiryHours = 1
        orderDetails.source = "mobile"
        orderDetails.description = "By Aagam"
        orderDetails.showShippingDetails = true
        orderDetails.showBackButton = true
        orderDetails.defaultGuestCheckout = true
        orderDetails.isCheckoutEmbed = false
        orderDetails.env = devEnvironment
        orderDetails.redirectUrl = "portone://checkout"
        orderDetails.environment = environment

        return orderDetails

    }

    fun getTokenizationRequest(): PaymentDto.CheckoutUsingTokenizationRequest {

        val orderId = Utility.getRandomString(10)
        val signatureHash =
            Utility.getSignatureHash(
                "50010",
                currency,
                "https://dev-checkout.portone.io/failure.html",
                orderId,
                clientKey,
                "https://dev-checkout.portone.io/success.html"
            )

        val paymentDetails = PaymentDto.CheckoutUsingTokenizationRequest(
            portoneKey = clientKey,
            paymentChannel = paymentChannel,
            paymentMethod = paymentMethod,
            merchantOrderId = orderId,
            amount = 50010.00,
            currency = currency,
            successUrl = "https://dev-checkout.portone.io/success.html",
            failureUrl = "https://dev-checkout.portone.io/failure.html",
            signatureHash = signatureHash,
            billingDetails = PaymentDto.BillingInfo(billingPhone = phoneNo),
            redirectUrl = "portone://checkout",
            source = "mobile",
            cardDetails = ExistingCardDetails,
            environment = environment,
            env = devEnvironment
        )
        return paymentDetails
    }

    fun getTokenizationWithNewCardRequest(): PaymentDto.CheckoutUsingTokenizationRequest {

        val orderId = Utility.getRandomString(10)
        val signatureHash =
            Utility.getSignatureHash(
                "50010",
                currency,
                "https://dev-checkout.portone.io/failure.html",
                orderId,
                clientKey,
                "https://dev-checkout.portone.io/success.html"
            )

        val paymentDetails = PaymentDto.CheckoutUsingTokenizationRequest(
            portoneKey = clientKey,
            paymentChannel = paymentChannel,
            paymentMethod = paymentMethod,
            merchantOrderId = orderId,
            amount = 50010.00,
            currency = currency,
            successUrl = "https://dev-checkout.portone.io/success.html",
            failureUrl = "https://dev-checkout.portone.io/failure.html",
            signatureHash = signatureHash,
            billingDetails = PaymentDto.BillingInfo(billingPhone = phoneNo),
            cardDetails = null,
            environment = environment,
            env = devEnvironment,
            redirectUrl = "portone://checkout",
            source = "mobile"
        )
        return paymentDetails
    }

    fun getWithoutTokenizationRequest(): PaymentDto.CheckoutWithoutTokenizationRequest {
        val orderId = Utility.getRandomString(10)
        val signatureHash =
            Utility.getSignatureHash(
                "50010",
                currency,
                "https://dev-checkout.portone.io/failure.html",
                orderId,
                clientKey,
                "https://dev-checkout.portone.io/success.html"
            )

        val billingAddress: PaymentDto.Address = PaymentDto.Address()
        billingAddress.city = "VND"
        billingAddress.countryCode = "VN"
        billingAddress.locale = "en"
        billingAddress.line_1 = "address"
        billingAddress.line_2 = "address_2"
        billingAddress.postal_code = "400202"
        billingAddress.state = "Mah"
        val shippingAddress: PaymentDto.Address = billingAddress

        val billingDetails: PaymentDto.BillingDetails = PaymentDto.BillingDetails()
        billingDetails.billingName = "Test mark"
        billingDetails.billingEmail = "markweins@gmail.com"
        billingDetails.billingPhone = phoneNo
        billingDetails.billingAddress = billingAddress

        val shippingDetails: PaymentDto.ShippingDetails = PaymentDto.ShippingDetails()
        shippingDetails.shippingName = "Test mark"
        shippingDetails.shippingEmail = "markweins@gmail.com"
        shippingDetails.shippingPhone = phoneNo
        shippingDetails.shippingAddress = shippingAddress

        val orderDetail: PaymentDto.OrderDetail = PaymentDto.OrderDetail()
        orderDetail.id = "knb"
        orderDetail.name = "kim nguyen bao"
        orderDetail.price = 1000.00
        orderDetail.quantity = 1
        val orderList = ArrayList<PaymentDto.OrderDetail>()
        orderList.add(orderDetail)

        val orderDetails = PaymentDto.CheckoutWithoutTokenizationRequest()
        orderDetails.key = clientKey
        orderDetails.paymentChannel = paymentChannel
        orderDetails.paymentMethod = paymentMethod
        orderDetails.merchantOrderId = orderId
        orderDetails.amount = 50010.00
        orderDetails.currency = currency
        orderDetails.signatureHash = signatureHash
        orderDetails.env = devEnvironment
        orderDetails.billingDetails = billingDetails
        orderDetails.shippingDetails = shippingDetails
        orderDetails.orderDetails = orderList
        orderDetails.successUrl = "https://dev-checkout.portone.io/success.html"
        orderDetails.failureUrl = "https://dev-checkout.portone.io/failure.html"
        orderDetails.redirectUrl = "portone://checkout"
        orderDetails.environment = environment
        orderDetails.source = "mobile"

        return orderDetails
    }

    fun getBankListRequest(): PaymentDto.BankListRequest {
        val request = PaymentDto.BankListRequest()
        request.amount = 20023.00
        request.environment = environment
        request.portoneKey = clientKey
        request.isMerchantSponsored = false
        request.paymentMethod = "GBPRIMEPAY_INSTALLMENT"
        request.overrideDefault = false
        request.currency= THB
        return request
    }


}