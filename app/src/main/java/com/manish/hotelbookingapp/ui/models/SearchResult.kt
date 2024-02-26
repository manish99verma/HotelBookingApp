package com.manish.hotelbookingapp.ui.models

import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.util.Event


data class SearchResult(
    val isLoading: Boolean = false,
    val error: Event<String>? = null,
    val appliedFilter: FilterResult,
    val result: List<Property>? = null,
    val cityId: Long
)
