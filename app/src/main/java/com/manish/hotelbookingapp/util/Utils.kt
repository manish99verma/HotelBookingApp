package com.manish.hotelbookingapp.util

import com.google.firestore.v1.StructuredAggregationQuery.Aggregation.Count
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
}