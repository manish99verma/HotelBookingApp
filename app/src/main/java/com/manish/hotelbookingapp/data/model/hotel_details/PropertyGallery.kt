package com.manish.hotelbookingapp.data.model.hotel_details

data class PropertyGallery(
    val __typename: String,
    val accessibilityLabel: String,
    val images: List<Image>,
    val imagesGrouped: Any,
    val mediaGalleryDialog: MediaGalleryDialog,
    val thumbnailGalleryDialog: ThumbnailGalleryDialog
)