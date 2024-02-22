package com.manish.hotelbookingapp.data.model.regions

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("@type")
    val atType: String,
    val cityId: String?,
    val coordinates: Coordinates,
    val essId: EssId,
    val gaiaId: String,
    val hierarchyInfo: HierarchyInfo,
    val hotelAddress: HotelAddress,
    val hotelId: String,
    val index: String,
    val regionNames: RegionNames,
    val type: String
)