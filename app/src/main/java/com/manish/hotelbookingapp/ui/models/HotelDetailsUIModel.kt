package com.manish.hotelbookingapp.ui.models

import com.manish.hotelbookingapp.data.model.hotel_details.HotelDetailsResult
import com.manish.hotelbookingapp.util.Event

data class HotelDetailsUIModel(
    val hotelId: String,
    val isLoading: Boolean = false,
    val error: Event<String>? = null,
    val data: HotelDetailsResult? = null,
)