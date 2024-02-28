package com.manish.hotelbookingapp.data.model.hotel_details

data class Amenities(
    val __typename: String,
    val amenities: List<Amenity>,
    val amenitiesDialog: AmenitiesDialog,
    val takeover: Takeover,
    val topAmenities: TopAmenities
)