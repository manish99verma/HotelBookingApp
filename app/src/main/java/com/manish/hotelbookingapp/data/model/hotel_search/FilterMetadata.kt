package com.manish.hotelbookingapp.data.model.hotel_search

data class FilterMetadata(
    val __typename: String,
    val amenities: List<Amenity>,
    val neighborhoods: List<Neighborhood>,
    val priceRange: PriceRange
)