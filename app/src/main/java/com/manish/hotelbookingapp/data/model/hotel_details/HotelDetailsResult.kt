package com.manish.hotelbookingapp.data.model.hotel_details

data class HotelDetailsResult(
    val __typename: String,
    val propertyContentSectionGroups: PropertyContentSectionGroups,
    val propertyGallery: PropertyGallery,
    val reviewInfo: ReviewInfo,
    val summary: Summary
)