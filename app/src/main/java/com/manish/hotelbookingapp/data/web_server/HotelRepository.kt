package com.manish.hotelbookingapp.data.web_server

import com.manish.hotelbookingapp.data.model.hotel_details.HotelDetailsResult
import com.manish.hotelbookingapp.data.model.hotel_search.HotelSearchResult
import com.manish.hotelbookingapp.data.model.regions.RegionsResult
import retrofit2.Response

class HotelRepository(private val hotelWebDataSource: HotelsApiService) : HotelRepositoryInterface {
    override suspend fun getRegions(
        query: String,
        domain: String,
        locale: String
    ): Response<RegionsResult> {
        return hotelWebDataSource.getRegions(query, domain, locale)
    }

    override suspend fun getHotels(
        region_id: Long,
        locale: String,
        checkin_date: String,
        sort_order: String,
        adults_number: Long,
        domain: String,
        checkout_date: String,
        price_min: Long,
        page_no: Int,
        price_max: Long,
        amenities: String
    ): Response<HotelSearchResult> {
        return hotelWebDataSource.getHotels(
            region_id,
            locale,
            checkin_date,
            sort_order,
            adults_number,
            domain,
            checkout_date,
            price_min,
            page_no,
            price_max,
            amenities
        )
    }

    override suspend fun getHotelDetails(
        domain: String,
        hotelId: Long,
        locale: String
    ): Response<HotelDetailsResult> {
        return hotelWebDataSource.getHotelDetails(domain, hotelId, locale)
    }

}

interface HotelRepositoryInterface {
    suspend fun getRegions(
        query: String,
        domain: String,
        locale: String
    ): Response<RegionsResult>

    suspend fun getHotels(
        region_id: Long,
        locale: String,
        checkin_date: String,
        sort_order: String,
        adults_number: Long,
        domain: String,
        checkout_date: String,
        price_min: Long,
        page_no: Int,
        price_max: Long,
        amenities: String
    ): Response<HotelSearchResult>

    suspend fun getHotelDetails(
        domain: String,
        hotelId: Long,
        locale: String
    ): Response<HotelDetailsResult>
}