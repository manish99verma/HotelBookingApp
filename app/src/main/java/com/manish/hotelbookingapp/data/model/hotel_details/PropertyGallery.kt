package com.manish.hotelbookingapp.data.model.hotel_details

import com.manish.hotelbookingapp.data.model.hotel_search.PropertyImage

data class PropertyGallery(
    val __typename: String,
    val accessibilityLabel: String,
    val images: List<PropertyImage>,
    val imagesGrouped: Any?,
    val mediaGalleryDialog: MediaGalleryDialog,
    val thumbnailGalleryDialog: ThumbnailGalleryDialog
)