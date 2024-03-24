package com.manish.hotelbookingapp.util

import android.content.Context
import android.content.Intent
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation.Count
import com.google.gson.Gson
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.ui.activities.HotelDetailsActivity
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.ui.viewmodels.HotelDetailsViewModel
import kotlin.math.roundToInt

object Utils {
    fun trimToOneDecimalPoint(input: Double): Double {
        val i = (input * 10).roundToInt()
        return i / 10.0
    }

    fun singularPulral(count: Int, singular: String, pulral: String): String {
        if (count < 2)
            return singular
        return pulral
    }

    fun openHotelDetailsActivity(
        context: Context,
        property: Property,
        bookingDetails: SearchFragmentUiModel
    ) {
        val hotelViewIntent = Intent(context, HotelDetailsActivity::class.java)

        val serialized = Gson().toJson(property)
        val detailsSerialized = Gson().toJson(bookingDetails)

        hotelViewIntent.putExtra("property", serialized)
        hotelViewIntent.putExtra("booking_details", detailsSerialized)

        context.startActivity(hotelViewIntent)
    }
}