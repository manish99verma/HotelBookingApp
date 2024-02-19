package com.manish.hotelbookingapp.data.model.hotel_search

data class PriceMetadata(
    val __typename: String,
    val discountType: String,
    val rateDiscount: RateDiscount,
    val totalDiscountPercentage: Int
)