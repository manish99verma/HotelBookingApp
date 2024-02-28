package com.manish.hotelbookingapp.data.model.hotel_details

data class Summary(
    val __typename: String,
    val highlightMessage: Any?,
    val overallScoreWithDescriptionA11y: OverallScoreWithDescriptionA11y,
    val propertyReviewCountDetails: PropertyReviewCountDetails,
    val seeAllAction: SeeAllAction,
    val location: Location
)