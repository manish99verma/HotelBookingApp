package com.manish.hotelbookingapp.data.model.hotel_search

data class Summary(
    val __typename: String,
    val loyaltyInfo: LoyaltyInfo,
    val matchedPropertiesSize: Int,
    val pricingScheme: PricingScheme,
    val regionCompression: Any,
    val resultsSummary: List<ResultsSummary>,
    val resultsTitleModel: ResultsTitleModel
)