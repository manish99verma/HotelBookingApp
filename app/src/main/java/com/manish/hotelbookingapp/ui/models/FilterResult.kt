package com.manish.hotelbookingapp.ui.models

data class FilterResult(
    val sortOrder: Sort,
    val rating: Int,
    val priceRange: Pair<Int, Int>
)

enum class Sort(val value: String) {
    REVIEW("Review"),
    RECOMMENDED("Recommended"),
    DISTANCE("Distance"),
    PRICE_LOW_TO_HIGH("Price Low to High"),
    PROPERTY_CLASS("Property Class"),
    PRICE_RELEVANT("Price Relevant"),
}