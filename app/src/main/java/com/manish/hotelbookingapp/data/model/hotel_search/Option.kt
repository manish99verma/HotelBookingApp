package com.manish.hotelbookingapp.data.model.hotel_search

data class Option(
    val __typename: String,
    val disclaimer: Disclaimer,
    val formattedDisplayPrice: String,
    val strikeOut: StrikeOut,
    val analytics: Analytics,
    val default: Boolean,
    val description: Any,
    val deselectAnalytics: DeselectAnalytics,
    val disabled: Boolean,
    val icon: Icon,
    val id: String,
    val primary: String,
    val secondary: Any,
    val selectAnalytics: SelectAnalytics,
    val selected: Boolean,
    val value: String
)