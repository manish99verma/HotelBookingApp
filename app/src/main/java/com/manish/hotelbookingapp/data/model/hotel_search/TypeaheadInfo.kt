package com.manish.hotelbookingapp.data.model.hotel_search

data class TypeaheadInfo(
    val __typename: String,
    val client: String,
    val isDestination: Boolean,
    val lineOfBusiness: String,
    val maxNumberOfResults: Int,
    val packageType: Any,
    val personalize: Boolean,
    val regionType: Int,
    val typeaheadFeatures: String
)