package com.manish.hotelbookingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel

@Entity(tableName = "booked_hotel")
data class BookedHotel(
    @PrimaryKey
    val bookingId: String,
    val property: Property,
    val bookingDetails: SearchFragmentUiModel
)
