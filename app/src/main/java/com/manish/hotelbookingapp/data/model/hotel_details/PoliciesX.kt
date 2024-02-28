package com.manish.hotelbookingapp.data.model.hotel_details

data class PoliciesX(
    val __typename: String,
    val checkinEnd: Any?,
    val checkinInstructions: List<String>,
    val checkinMinAge: Any?,
    val checkinStart: Any?,
    val checkoutTime: Any?,
    val childAndBed: ChildAndBed,
    val needToKnow: NeedToKnow,
    val paymentOptions: List<Any>,
    val pets: Pets,
    val shouldMention: ShouldMention
)