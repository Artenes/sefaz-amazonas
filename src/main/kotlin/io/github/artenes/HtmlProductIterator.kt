package io.github.artenes

import org.jsoup.select.Elements

/**
 * Iterator used to get through the list of spans that form the list
 * of attributes of a series of products in a html page
 */
class HtmlProductIterator(private val elements: Elements?) : Iterator<HtmlProduct> {

    private var index = 0

    override fun hasNext(): Boolean {
        return elements != null && index < elements.size
    }

    override fun next(): HtmlProduct {
        //this list will have the data of each item in a interval of 32 fields
        val productIndex = elements?.get(index + 0)?.text() ?: ""
        val productName = elements?.get(index + 1)?.text() ?: ""
        val productAmount = elements?.get(index + 2)?.text() ?: ""
        val productUnit = elements?.get(index + 3)?.text() ?: ""
        val productPrice = elements?.get(index + 4)?.text() ?: ""
        val productCode = elements?.get(index + 5)?.text() ?: ""
        val productBarCode = elements?.get(index + 18)?.text() ?: ""
        val productTax = elements?.get(index + 25)?.text() ?: ""

        //if the product does not has taxes, there is less fields to go trough
        //so sum up 30 instead of 32 fields to go to the next item
        index += if (productTax == "0.00") {
            30
        } else {
            32
        }

        return HtmlProduct(productIndex, productCode, productName, productAmount, productUnit, productPrice, productTax, productBarCode)
    }

}