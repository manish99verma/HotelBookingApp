package com.manish.hotelbookingapp.data.model.hotel_search

data class RevealAction(
    val __typename: String,
    val accessibility: String,
    val action: Action,
    val badge: Int,
    val disabled: Boolean,
    val icon: Icon,
    val primary: String
)