package com.manish.hotelbookingapp.data.model.hotel_search

data class DropdownFilterOption(
    val __typename: String,
    val analytics: Analytics,
    val default: Boolean,
    val description: Any,
    val deselectAnalytics: DeselectAnalytics,
    val disabled: Boolean,
    val icon: Any,
    val id: String,
    val primary: String,
    val secondary: Any,
    val selectAnalytics: SelectAnalytics,
    val selected: Boolean,
    val value: String
)