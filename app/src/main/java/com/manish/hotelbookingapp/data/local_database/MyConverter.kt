package com.manish.hotelbookingapp.data.local_database

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manish.hotelbookingapp.data.model.hotel_search.OfferBadge
import com.manish.hotelbookingapp.data.model.hotel_search.PropertyDestinationInfo
import com.manish.hotelbookingapp.data.model.hotel_search.PropertyImage
import com.manish.hotelbookingapp.data.model.hotel_search.PropertyPrice
import com.manish.hotelbookingapp.data.model.hotel_search.PropertyReviewsSummary


class MyConverter {
    private fun <T> serialize(obj: T?): String {
        if (obj == null)
            return ""
        return Gson().toJson(obj)
    }

    @TypeConverter
    public fun serializePropertyImage(propertyImage: PropertyImage): String {
        return serialize(propertyImage)
    }

    @TypeConverter
    public fun deSerializePropertyImage(data: String): PropertyImage? {
        if (data.isEmpty())
            return null
        val type = object : TypeToken<PropertyImage>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    public fun serializeOfferBadge(input: OfferBadge?): String {
        return serialize(input)
    }

    @TypeConverter
    public fun deSerializeOfferBadge(data: String): OfferBadge? {
        if (data.isEmpty())
            return null
        val type = object : TypeToken<OfferBadge>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    public fun serializeOfferBadge(input: PropertyReviewsSummary): String {
        return serialize(input)
    }

    @TypeConverter
    public fun deSerializePropertyReviewSummary(data: String): PropertyReviewsSummary? {
        if (data.isEmpty())
            return null
        val type = object : TypeToken<PropertyReviewsSummary>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    public fun serializeOfferBadge(input: PropertyDestinationInfo): String {
        return serialize(input)
    }

    @TypeConverter
    public fun deSerializePropertyPropertyDestinationInfo(data: String): PropertyDestinationInfo? {
        if (data.isEmpty())
            return null
        val type = object : TypeToken<PropertyDestinationInfo>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    public fun serializeOfferBadge(input: PropertyPrice): String {
        return serialize(input)
    }

    @TypeConverter
    public fun deSerializePropertyPropertyPrice(data: String): PropertyPrice? {
        if (data.isEmpty())
            return null
        val type = object : TypeToken<PropertyPrice>() {}.type
        return Gson().fromJson(data, type)
    }
}