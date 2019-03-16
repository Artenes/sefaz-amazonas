package io.github.artenes

data class SefazResponse(val content: String, val cookie: String)

data class Company(val cnpj: String, val name: String, val stateRegistration: String, val address: String, val district: String, val city: String, val state: String)

data class Product(val index: Int, val code: String, val name: String, val amount: Float, val unit: String, val price: Long, val unitPrice: Long, val tax: Long, val barcode: String)

data class Coupon(val accessKey: String, val dateTimeUnix: Long, val value: Long, val icmsBase: Long, val icms: Long, val tax: Long, val company: Company, val products: List<Product>)

data class HtmlProduct(val index: String, val code: String, val name: String, val amount: String, val unit: String, val total: String, val unitPrice: String, val tax: String, val barcode: String)