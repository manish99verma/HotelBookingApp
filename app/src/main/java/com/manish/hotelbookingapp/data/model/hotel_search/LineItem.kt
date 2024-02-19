package com.manish.hotelbookingapp.data.model.hotel_search

data class LineItem(
    val __typename: String,
    val accessibilityLabel: Any,
    val badge: Any,
    val disclaimer: Disclaimer,
    val mark: Any,
    val price: Price,
    val role: String,
    val state: String,
    val value: String,
)