package io.github.artenes

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * API to access details of coupons from Sefaz website's
 */
class SefazAmazonas {

    private val httpClient = OkHttpClient()
    private val parser = SefazCouponParser()

    /**
     * Get a coupon from a url to access counpon data:
     * https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=1234567890
     */
    fun getCouponFromUrl(url: String): Coupon {

        //the first one gets the basic data of the coupon
        val firstResponse = getResponseFromUrl(url)
        //the second gets all the necessary data
        val secondResponse = getResponseDetailsFromCookie(firstResponse.cookie)
        //then we just parse it
        return parser.parse(secondResponse.content)

    }

    /**
     * Get a raw response with basic data of the coupon
     *
     * @param url the url to accces a coupon: https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=1234567890
     */
    internal fun getResponseFromUrl(url: String): SefazResponse {

        val request = Request.Builder().url(url).build()

        val call = httpClient.newCall(request)

        return call.execute().use {

            val jsessionId = """(?<=JSESSIONID=)\w+""".toRegex()

            val headers = it.header("Set-Cookie") ?: ""
            val body = it.body()?.string() ?: ""
            val cookie = jsessionId.find(headers)?.value ?: ""

            SefazResponse(body, cookie)
        }

    }

    /**
     * Get a raw response with more details of a coupon
     *
     * @param cookie the cookie to access the details of a coupon
     */
    internal fun getResponseDetailsFromCookie(cookie: String): SefazResponse {

        val request = Request.Builder()
                .url("https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.do?acao=abrirAbas")
                .addHeader("Cookie", "JSESSIONID=$cookie")
                .build()

        val call = httpClient.newCall(request)

        return call.execute().use {

            val body = it.body()?.string() ?: ""

            SefazResponse(body, cookie)

        }

    }

}