package com.manish.hotelbookingapp.data.web_server

import com.manish.hotelbookingapp.data.model.hotel_details.HotelDetailsResult
import com.manish.hotelbookingapp.data.model.hotel_search.HotelSearchResult
import com.manish.hotelbookingapp.data.model.regions.RegionsResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HotelsApiService {
    @GET("regions")
    suspend fun getRegions(
        @Query("query") query: String,
        @Query("domain") domain: String,
        @Query("locale") locale: String
    ): Response<RegionsResult>

    @GET("hotels/search")
    suspend fun getHotels(
        @Query("region_id") region_id: Long,
        @Query("locale") locale: String,
        @Query("checkin_date") checkin_date: String,
        @Query("sort_order") sort_order: String,
        @Query("adults_number") adults_number: Long,
        @Query("domain") domain: String,
        @Query("checkout_date") checkout_date: String,
        @Query("price_min") price_min: Long,
        @Query("page_no") page_no: Int,
        @Query("price_max") price_max: Long,
        @Query("amenities") amenities: String,
        @Query("loading_type") loadingType:String = "HOTEL,HOSTEL,APART_HOTEL"
    ): Response<HotelSearchResult>

    @GET("hotels/details")
    suspend fun getHotelDetails(
        @Query("domain") domain: String,
        @Query("hotel_id") hotelId: Long,
        @Query("locale") locale: String
    ):Response<HotelDetailsResult>
}