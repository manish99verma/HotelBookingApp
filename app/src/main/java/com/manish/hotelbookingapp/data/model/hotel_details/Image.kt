package com.manish.hotelbookingapp.data.model.hotel_details

data class Image(
    val __typename: String,
    val accessibilityText: String,
    val actionLabel: String,
    val image: Image,
    val imageId: String,
    val description: String,
    val url: String
)