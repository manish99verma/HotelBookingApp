package com.manish.hotelbookingapp.data.model.hotel_search

data class PropertySearchListings(
    val __typename: String,
    val availability: Availability,
    val destinationInfo: DestinationInfo,
    val featuredMessages: List<Any>,
    val id: String,
    val legalDisclaimer: Any,
    val listingFooter: Any,
    val mapMarker: MapMarker,
    val name: String,
    val neighborhood: Neighborhood,
    val offerBadge: OfferBadge,
    val offerSummary: OfferSummary,
    val pinnedDetails: Any,
    val price: Price,
    val priceAfterLoyaltyPointsApplied: PriceAfterLoyaltyPointsApplied,
    val priceMetadata: PriceMetadata,
    val propertyFees: List<Any>,
    val propertyImage: PropertyImage,
    val regionId: String,
    val reviews: Reviews,
    val saveTripItem: Any,
    val star: Any,
    val supportingMessages: Any
)