package com.manish.hotelbookingapp.data.model.hotel_details

data class Property(
    val __typename: String,
    val header: Header,
    val icon: Icon,
    val items: List<Item>,
    val jumpLink: Any
)