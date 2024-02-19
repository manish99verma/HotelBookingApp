package com.manish.hotelbookingapp.data.model.hotel_search

data class HotelSearchResult(
    val __typename: String,
    val clickstream: Clickstream,
    val filterMetadata: FilterMetadata,
    val map: Map,
    val properties: List<Property>,
    val propertySearchListings: List<PropertySearchListings>,
    val searchCriteria: SearchCriteria,
    val shoppingContext: ShoppingContext,
    val summary: Summary,
    val universalSortAndFilter: UniversalSortAndFilter
)