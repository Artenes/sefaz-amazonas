package io.github.artenes

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Parses a HTML page of a coupon from Sefaz's websites
 */
class SefazCouponParser {

    /**
     * Parse an html page to a coupon
     */
    fun parse(html: String): Coupon {

        val extractor = HtmlCouponData(html)
        return parse(extractor)

    }

    /**
     * Use the given couponData to create coupon
     * This class is used mostly in tests
     *
     * @param couponData the data to use to create the coupon
     * @throws IllegalArgumentException if any give data is invalid
     */
    internal fun parse(couponData: CouponData): Coupon {

        if (!couponData.hasEnoughDataToBeParsed()) {
            throw IllegalArgumentException("The content from the given url is invalid (not enough data to be parsed)")
        }

        try {
            return parseToCoupon(couponData)
        } catch (exception: StringIndexOutOfBoundsException) {
            throw IllegalArgumentException(exception.message, exception)
        } catch (exception: ParseException) {
            throw IllegalArgumentException(exception.message, exception)
        } catch (exception: NumberFormatException) {
            throw IllegalArgumentException(exception.message, exception)
        } catch (exception: IndexOutOfBoundsException) {
            throw IllegalArgumentException(exception.message, exception)
        }

    }

    /**
     * Parses the data to a coupon. Created mostly to resume
     * this huge logic block to a one method call
     */
    private fun parseToCoupon(couponData: CouponData): Coupon {

        //remove white space from access key
        val accessKey = couponData.accessKey.replace(" ", "")

        //extract the date from a field that has it then convert to unix timestamp
        val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(couponData.dateTime.subSequence(0, 18).toString()).time

        //remove the comma to convert a value to cents (e.g 34,12 would bge 3412 cents)
        val value = couponData.value.replace(",", "").toLong()

        //remove all specials characters from cnpj
        val companyCnpj = couponData.companyCnpj.replace(".", "").replace("/", "").replace("-", "")

        //extract the city name from a field that has it then trim it
        val companyCity = couponData.companyCity.split("-")[1].trim()

        val companyName = couponData.companyName
        val companyStateRegistration = couponData.companyStateRegistration
        val companyAddress = couponData.companyAddress
        val companyDistrict = couponData.companyDistrict
        val companyState = couponData.companyState

        //same logic, removing the comma will convert values to cents
        val icmsBase = couponData.icmsBase.replace(",", "").toLong()
        val icms = couponData.icms.replace(",", "").toLong()
        val tax = couponData.tax.replace(",", "").toLong()

        //create company instance
        val company = Company(companyCnpj, companyName, companyStateRegistration, companyAddress, companyDistrict, companyCity, companyState)

        //the list of products has a bunch of span tags, which will be easier to access instead
        // of going through the DOM tree
        val products = mutableListOf<Product>()
        val productIterator = couponData.productsIterator

        while (productIterator.hasNext()) {
            val htmlProduct = productIterator.next()

            val productIndex = htmlProduct.index.toInt()
            val productName = htmlProduct.name
            //conversion to cents
            val productAmount = htmlProduct.amount.replace(",", ".").toFloat()
            val productUnit = htmlProduct.unit
            val productTotal = htmlProduct.total.replace(",", "").toLong()
            val productCode = htmlProduct.code
            val productBarCode = htmlProduct.barcode
            val productTax = htmlProduct.tax.replace(".", "").toLong()
            //conversion from float to long, just to avoid rounding or approximation problems 15,4500 -> 15.4500 -> 15.45 -> 1545
            val productUnitPrice = htmlProduct.unitPrice.replace(",", ".").toFloat().toString().replace(".", "").toLong()

            val product = Product(productIndex, productCode, productName, productAmount, productUnit, productTotal, productUnitPrice, productTax, productBarCode)
            products.add(product)
        }

        return Coupon(accessKey, dateTime, value, icmsBase, icms, tax, company, products)

    }

}