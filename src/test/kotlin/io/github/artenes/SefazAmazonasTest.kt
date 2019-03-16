package io.github.artenes

import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SefazAmazonasTest {

    private lateinit var api: SefazAmazonas

    private val URL = "https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=13190322991939001927650080001108441061156583"

    @Before
    fun setUp() {
        api = SefazAmazonas()
    }

    @Test
    fun returns_coupon_code_and_cookie() {

        val response = api.getResponseFromUrl(URL)
        //the access key of the coupon is displayed in the coupon
        assertThat(response.content, containsString("1319 0322 9919 3900 1927 6500 8000 1108 4410 6115 6583"))
        assertTrue(response.cookie.isNotEmpty())

    }

    @Test
    fun returns_coupon_details_on_second_request() {

        val firstResponse = api.getResponseFromUrl(URL)
        val secondResponse = api.getResponseDetailsFromCookie(firstResponse.cookie)
        //digest value, this value only exists in the more details section of the coupon
        assertThat(secondResponse.content, containsString("qrk/oURwj6LR1Kzm0PoEiS5zmeM="))

    }

    @Test
    fun returns_coupon_details_on_one_request() {

        val coupon = api.getCouponFromUrl(URL)
        assertEquals("13190322991939001927650080001108441061156583", coupon.accessKey)
        assertEquals(1551887823000, coupon.dateTimeUnix)
        assertEquals(9852, coupon.value)
        assertEquals("22991939001927", coupon.company.cnpj)
        assertEquals("SUPERMERCADOS DB LTDA", coupon.company.name)
        assertEquals("042152208", coupon.company.stateRegistration)
        assertEquals("AM", coupon.company.state)
        assertEquals("RUA LINDON JOHNSON, 100", coupon.company.address)
        assertEquals("PQ.DEZ", coupon.company.district)
        assertEquals("MANAUS", coupon.company.city)
        assertEquals(8266, coupon.icmsBase)
        assertEquals(1487, coupon.icms)
        assertEquals(2738, coupon.tax)
        assertEquals(24, coupon.products.size)
        assertEquals(1, coupon.products.first().index)
        assertEquals("PAPEL TOALHA SCALA BCO C2 RL", coupon.products.first().name)
        assertEquals(1.0F, coupon.products.first().amount)
        assertEquals("RL", coupon.products.first().unit)
        assertEquals(359, coupon.products.first().price)
        assertEquals(65, coupon.products.first().tax)

    }

}