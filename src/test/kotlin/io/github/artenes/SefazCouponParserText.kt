package io.github.artenes

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SefazCouponParserText {

    private lateinit var parser: SefazCouponParser

    @Before
    fun setUp() {
        parser = SefazCouponParser()
    }

    @Test
    fun do_not_throws_exception_when_has_valid_data() {

        //all values different from an empty string are possible causes of exceptions

        val mockExtractor = Mockito.mock(CouponData::class.java)
        val mockIterator = Mockito.mock(Iterator::class.java)
        val mockProduct = HtmlProduct("1", "", "", "1", "", "1", "1,1000", "1", "")

        Mockito.`when`(mockIterator.hasNext()).thenReturn(true).thenReturn(false)
        Mockito.`when`(mockIterator.next()).thenReturn(mockProduct)
        Mockito.`when`(mockExtractor.hasEnoughDataToBeParsed()).thenReturn(true)

        Mockito.`when`(mockExtractor.accessKey).thenReturn("")
        Mockito.`when`(mockExtractor.dateTime).thenReturn("06/03/2019 11:57:38-04:00")
        Mockito.`when`(mockExtractor.value).thenReturn("1")
        Mockito.`when`(mockExtractor.companyCnpj).thenReturn("")
        Mockito.`when`(mockExtractor.companyCity).thenReturn("-")
        Mockito.`when`(mockExtractor.companyName).thenReturn("")
        Mockito.`when`(mockExtractor.companyStateRegistration).thenReturn("")
        Mockito.`when`(mockExtractor.companyAddress).thenReturn("")
        Mockito.`when`(mockExtractor.companyDistrict).thenReturn("")
        Mockito.`when`(mockExtractor.companyState).thenReturn("")
        Mockito.`when`(mockExtractor.icmsBase).thenReturn("1")
        Mockito.`when`(mockExtractor.icms).thenReturn("1")
        Mockito.`when`(mockExtractor.tax).thenReturn("1")
        Mockito.`when`(mockExtractor.productsIterator).thenReturn(mockIterator as Iterator<HtmlProduct>)

        parser.parse(mockExtractor)

    }

}