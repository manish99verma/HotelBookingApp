package com.manish.hotelbookingapp.data.model.hotel_details

data class Location(
    val __typename: String,
    val address: Address,
    val coordinates: Coordinates,
    val mapDialog: MapDialog,
    val multiCityRegion: MultiCityRegion,
    val staticImage: StaticImage,
    val whatsAround: WhatsAround
)