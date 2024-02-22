package com.manish.hotelbookingapp.data.model.hotel_search

data class Secondary(
    val __typename: String,
    val icon_temp: IconTemp,
    val mark: Any,
    val text: String?,
    val theme_temp: String,
    val action: Action,
    val disabled: Boolean,
    val primary: String
)