package com.manish.hotelbookingapp.data.model.hotel_search

data class Expando(
    val __typename: String,
    val collapseAnalytics: CollapseAnalytics,
    val collapseLabel: String,
    val expandAnalytics: ExpandAnalytics,
    val expandLabel: String,
    val threshold: Int
)