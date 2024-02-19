package com.manish.hotelbookingapp.data.model.hotel_search

data class Price(
    val __typename: String,
    val displayMessages: List<DisplayMessage>,
    val lead: Lead,
    val options: List<Option>,
    val priceMessages: List<PriceMessage>,
    val priceMessaging: Any,
    val strikeOut: StrikeOut,
    val strikeOutType: String,
    val accessibilityLabel: String,
    val formatted: String,
)