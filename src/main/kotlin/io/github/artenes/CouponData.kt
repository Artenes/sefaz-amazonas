package io.github.artenes

/**
 * Data related to a coupon that can be extracted from some source
 */
interface CouponData {
    /**
     * Define if there is enough data to be parsed
     *
     * @return true if it can, false otherwise
     */
    fun hasEnoughDataToBeParsed(): Boolean
    val accessKey: String
    val dateTime: String
    val value: String
    val companyCnpj: String
    val companyCity: String
    val companyName: String
    val companyStateRegistration: String
    val companyAddress: String
    val companyDistrict: String
    val companyState: String
    val icmsBase: String
    val icms: String
    val tax: String
    val productsIterator: Iterator<HtmlProduct>
}