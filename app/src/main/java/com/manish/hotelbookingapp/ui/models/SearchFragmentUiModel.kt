package com.manish.hotelbookingapp.ui.models

import java.util.Calendar

data class SearchFragmentUiModel(
    val city: String,
    val country: String,
    val cityId: Long,
    val startDate: Calendar,
    val endDate: Calendar,
    val guestsCount: Int,
    val roomsCount: Int
)
