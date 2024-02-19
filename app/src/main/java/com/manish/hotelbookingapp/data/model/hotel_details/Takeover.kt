package com.manish.hotelbookingapp.data.model.hotel_details

data class Takeover(
    val __typename: String,
    val amenityClosures: Any,
    val highlight: List<Highlight>,
    val `property`: List<Property>
)