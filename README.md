# Sefaz Amazonas parser

Library in kotlin (for JVM) that scraps a coupon details page to retrieve some relevant information such its total value, taxes and its list of products/services.

# Download

Go to the releases tab and download the latest jar file

# Usage

```kotlin

val api = SefazAmazonas()
val coupon = api.getCouponFromUrl("https://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp?p=13190322991939001927650080001108441061156583")

//available information
//all monetary values are in cents (Brazilian's Real)
coupon.accessKey
coupon.dateTimeUnix
coupon.value
coupon.icmsBase
coupon.icms
coupon.tax

//company details where the purchase was made
coupon.company.cnpj
coupon.company.name
coupon.company.stateRegistration
coupon.company.address
coupon.company.district
coupon.company.city
coupon.company.state

//products information
for (product in coupon.products) {
    product.index
    product.code
    product.name
    product.amount
    product.unit
    product.price
    product.tax
    product.barcode
}

```
