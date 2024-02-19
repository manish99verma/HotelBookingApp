package com.manish.hotelbookingapp.data.model.hotel_details

data class Highlight(
    val __typename: String,
    val header: Header,
    val icon: Any,
    val items: List<Item>,
    val jumpLink: Any
)