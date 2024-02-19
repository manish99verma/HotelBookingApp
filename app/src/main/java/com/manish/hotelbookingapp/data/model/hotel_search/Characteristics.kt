package com.manish.hotelbookingapp.data.model.hotel_search

data class Characteristics(
    val __typename: String,
    val labels: List<Label>,
    val max: Int,
    val min: Int,
    val step: Int
)