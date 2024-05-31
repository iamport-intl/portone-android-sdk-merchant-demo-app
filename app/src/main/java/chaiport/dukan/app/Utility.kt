package chaiport.dukan.app

import android.util.Base64
import android.util.Log
import com.android.chaipaylibrary.constants.TAG_CHAI_PAY
import com.google.common.hash.Hashing
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap


object Utility {

    internal fun getSignatureHash(
        amount: String,
        currency: String,
        failureUrl: String,
        orderId: String,
        clientKey: String,
        successUrl: String
    ): String {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["amount"] = amount
        hashMap["currency"] = currency
        hashMap["failure_url"] = failureUrl
        hashMap["merchant_order_id"] = orderId
        hashMap["client_key"] = clientKey
        hashMap["success_url"] = successUrl

        val message = StringBuilder()
        for ((key, value) in hashMap.toSortedMap().entries) {
            val values = URLEncoder.encode(value, "UTF-8")
            if (message.isNotEmpty()) {
                message.append("&$key=$values")
            } else {
                message.append("$key=$values")
            }
        }

        val sha256 = Hashing.hmacSha256(RequestBodies.secretKey.toByteArray(StandardCharsets.UTF_8))
            .hashString(message, StandardCharsets.UTF_8).asBytes()

        val base64: String = Base64.encodeToString(sha256, Base64.DEFAULT)
        Log.i(TAG_CHAI_PAY, "SignatureHash:base64-> $base64")

        return base64.trim()
    }

    internal fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    internal fun getJwtToken(): String {
        val cur = Calendar.getInstance().time
        val generationTime: Long = cur.time / 1000
        val expirationTime: Long = generationTime + 100

        val jwt = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("alg", "HS256")
            .claim("iss", "PORTONE")
            .claim("sub", RequestBodies.clientKey)
            .claim("iat", generationTime)
            .claim("exp", expirationTime)
            .signWith(
                SignatureAlgorithm.HS256,
                RequestBodies.secretKey.toByteArray(
                    StandardCharsets.UTF_8
                )
            )
            .compact()

        return "Bearer $jwt"
    }
}