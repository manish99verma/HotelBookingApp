package com.manish.hotelbookingapp.data.model.hotel_search

data class Availability(
    val __typename: String,
    val available: Boolean,
    val minRoomsLeft: Int
)