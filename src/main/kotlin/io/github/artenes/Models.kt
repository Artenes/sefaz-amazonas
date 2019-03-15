package io.github.artenes

data class SefazResponse(val content: String, val cookie: String)

data class Company(val cnpj: String, val name: String, val stateRegistration: String, val address: String,
                   val district: String, val city: String, val state: String)

data class Product(val index: Int, val name: String, val amount: Float, val unit: String, val price: Long, val tax: Long)

data class Coupon(val accessKey: String, val dateTimeUnix: Long, val value: Long, val icmsBase: Long, val icms: Long,
                  val tax: Long, val company: Company, val products: List<Product>)