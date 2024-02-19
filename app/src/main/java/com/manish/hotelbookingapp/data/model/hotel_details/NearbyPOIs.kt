package com.manish.hotelbookingapp.data.model.hotel_details

data class NearbyPOIs(
    val __typename: String,
    val header: Header,
    val icon: Any,
    val items: List<Item>,
    val jumpLink: Any
)