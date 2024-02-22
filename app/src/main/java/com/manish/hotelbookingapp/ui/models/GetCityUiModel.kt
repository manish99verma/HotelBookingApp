package com.manish.hotelbookingapp.ui.models

import com.manish.hotelbookingapp.util.Event

data class GetCityUiModel(
    val searching: Boolean = false,
    val cityCode: Event<String>? = null,
    var error: Event<String>? = null
)
