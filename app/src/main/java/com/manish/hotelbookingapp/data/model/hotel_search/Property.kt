package com.manish.hotelbookingapp.data.model.hotel_search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotel_search_result_property")
data class Property(
    @PrimaryKey
    val id: String,
    val propertyImage: PropertyImage?,
    val offerBadge: OfferBadge?,
    val reviews: PropertyReviewsSummary?,
    val name: String?,
    val destinationInfo: PropertyDestinationInfo?,
    val price: PropertyPrice?
)
