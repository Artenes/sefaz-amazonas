package io.github.artenes

import org.jsoup.Jsoup
import java.text.SimpleDateFormat

/**
 * Parses a HTML page of a coupon from Sefaz's websites
 */
class SefazCouponParser {

    fun parse(html: String): Coupon {

        val document = Jsoup.parse(html)

        //it is easier to get all span tags than go through the whole html tree that it is a huge mess
        //so we use set indexes to access this data, if one is out of place this will give wrong data
        val spans = document.getElementsByTag("span")

        //remove white space from access key
        val accessKey = spans[0].text().replace(" ", "")

        //extract the date from a field that has it then convert to unix timestamp
        val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(spans[6].text()
                .subSequence(0, 18).toString()).time

        //remove the comma to convert a value to cents (e.g 34,12 would bge 3412 cents)
        val value = spans[8].text().replace(",", "").toLong()

        //remove all specials characters from cnpj
        val companyCnpj = spans[9].text().replace(".", "")
                .replace("/", "").replace("-", "")

        //extract the city name from a field that has it then trim it
        val companyCity = spans[37].text().split("-")[1].trim()

        val companyName = spans[10].text()
        val companyStateRegistration = spans[11].text()
        val companyAddress = spans[34].text()
        val companyDistrict = spans[35].text()
        val companyState = spans[12].text()

        //same logic, removing the comma will convert values to cents
        val icmsBase = spans[59].text().replace(",", "").toLong()
        val icms = spans[60].text().replace(",", "").toLong()
        val tax = spans[73].text().replace(",", "").toLong()

        //create company instance
        val company = Company(companyCnpj, companyName, companyStateRegistration, companyAddress,
                companyDistrict, companyCity, companyState)

        //the list of products has a bunch of span tags, which will be easier to access instead
        // of going through the DOM tree
        val products = mutableListOf<Product>()
        val productsTags = document.getElementById("Prod").getElementsByTag("span")
        var index = 0

        //this list will have the data of each item in a interval of 32 fields
        while (index < productsTags.size) {
            val productIndex = productsTags[index + 0].text().toInt()
            val productName = productsTags[index + 1].text()
            //conversion to cents
            val productAmount = productsTags[index + 2].text().replace(",", ".").toFloat()
            val productUnit = productsTags[index + 3].text()
            val productPrice = productsTags[index + 4].text().replace(",", "").toLong()
            val productTax = productsTags[index + 25].text().replace(".", "").toLong()

            val product = Product(productIndex, productName, productAmount, productUnit, productPrice, productTax)
            products.add(product)

            //if the product does not has taxes, there is less fields to go trough
            //so sum up 30 instead of 32 fields to go to the next item
            index += if (productTax == 0L) {
                30
            } else {
                32
            }
        }

        return Coupon(accessKey, dateTime, value, icmsBase, icms, tax, company, products)

    }

}