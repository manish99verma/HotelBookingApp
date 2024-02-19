package com.manish.hotelbookingapp.data.model.hotel_details

data class Summary(
    val __typename: String,
    val highlightMessage: Any,
    val overallScoreWithDescriptionA11y: OverallScoreWithDescriptionA11y,
    val propertyReviewCountDetails: Any,
    val seeAllAction: SeeAllAction,
    val amenities: Amenities,
    val bannerMessage: Any,
    val cleaningAndSafety: Any,
    val featuredMessages: Any,
    val id: String,
    val latinAlphabetName: String,
    val location: Location,
    val lodgingChatbot: Any,
    val name: String,
    val nearbyPOIs: NearbyPOIs,
    val overview: Overview,
    val spaceOverview: Any,
    val starRatingIcon: StarRatingIcon,
    val tagline: String
)