package io.github.artenes

import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * Extractor that will expose some data from a coupon from a html page
 */
class HtmlCouponData(html: String) : CouponData {

    private val document = Jsoup.parse(html)

    //it is easier to get all span tags than go through the whole html tree that it is a huge mess
    //so we use set indexes to access this data, if one is out of place this will give wrong data
    private val spans: Elements = document.getElementsByTag("span")

    //this list will have the data of each product or service in a interval of 32 fields
    //so every 32 fields (or spans) there will be a new product or service
    private val products: Elements? = document.getElementById("Prod")?.getElementsByTag("span")

    override fun hasEnoughDataToBeParsed(): Boolean {
        //74 is the minimum amount of spans that we need to be certain that the data we want is there
        return spans.isNotEmpty() && spans.size > 74 && products != null && products.isNotEmpty()
    }

    override val accessKey: String
        get() {
            return spans[0].text()
        }

    override val dateTime: String
        get() {
            return spans[6].text()
        }

    override val value: String
        get() {
            return spans[8].text()
        }

    override val companyCnpj: String
        get() {
            return spans[9].text()
        }

    override val companyCity: String
        get() {
            return spans[37].text()
        }

    override val companyName: String
        get() {
            return spans[10].text()
        }

    override val companyStateRegistration: String
        get() {
            return spans[11].text()
        }

    override val companyAddress: String
        get() {
            return spans[34].text()
        }

    override val companyDistrict: String
        get() {
            return spans[35].text()
        }

    override val companyState: String
        get() {
            return spans[12].text()
        }

    override val icmsBase: String
        get() {
            return spans[59].text()
        }

    override val icms: String
        get() {
            return spans[60].text()
        }

    override val tax: String
        get() {
            return spans[73].text()
        }

    override val productsIterator: Iterator<HtmlProduct> by lazy {
        HtmlProductIterator(products)
    }

}