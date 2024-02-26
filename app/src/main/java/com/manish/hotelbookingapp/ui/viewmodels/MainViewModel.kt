package com.manish.hotelbookingapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.hotel_search.Property
import com.manish.hotelbookingapp.data.model.regions.RegionsResult
import com.manish.hotelbookingapp.data.web_server.HotelRepositoryInterface
import com.manish.hotelbookingapp.ui.models.FilterResult
import com.manish.hotelbookingapp.ui.models.GetCityUiModel
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.ui.models.SearchResult
import com.manish.hotelbookingapp.ui.sign_in.User
import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.Result
import com.manish.hotelbookingapp.util.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val auth: FirebaseAuth,
    private val hotelRepository: HotelRepositoryInterface
) : AndroidViewModel(application) {
    private val resources = application.resources
    private val _cityIdSearchState = MutableLiveData<GetCityUiModel>()
    val citySearchResult: LiveData< GetCityUiModel> get() = _cityIdSearchState
    var searchFragmentState: SearchFragmentUiModel? = null
        private set

    private lateinit var navigateToSearchResultCallback: ((String) -> Unit)
    private val _recommendedHotels = MutableLiveData<SearchResult>()
    val dataRecommendedHotel: LiveData<SearchResult> get() = _recommendedHotels
    private val _businessHotels = MutableLiveData<SearchResult>()
    val dataBusinessHotels: LiveData<SearchResult> get() = _businessHotels

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): User? {
        val user = auth.currentUser ?: return null
        return User(user.uid, user.displayName, user.email, user.photoUrl.toString())
    }

    fun logOut() {
        auth.signOut()
    }

    fun getCityId(
        query: String,
        startDate: Calendar,
        endDate: Calendar,
        guestsCount: Int,
        roomsCount: Int
    ) {
        viewModelScope.launch(IO) {
            emitCitySearchState(isSearching = true)

            safeApiCall {
                val res = hotelRepository.getRegions(query, "IN", "en_IN")
                Result.Success(res)
            }.also {
                if (it is Result.Error) {
                    emitCitySearchState(error = Event(resources.getString(R.string.no_internet)))
                } else if (it is Result.Success) {
                    val res = it.data.body()

                    if (res != null) {
                        val cityId = getCityIdFromRegions(res)
                        if (cityId == null)
                            emitCitySearchState(error = Event("$query not found!"))
                        else {
                            saveHomeState(res, cityId, startDate, endDate, guestsCount, roomsCount)
                            emitCitySearchState(
                                cityId = Event(getCityIdFromRegions(res).toString())
                            )
                        }
                    } else {
                        emitCitySearchState(
                            error = Event(resources.getString(R.string.unknown_error_occurred))
                        )
                    }
                }
            }
        }
    }

    private fun emitCitySearchState(
        isSearching: Boolean = false,
        error: Event<String>? = null,
        cityId: Event<String>? = null
    ) {
        GetCityUiModel(
            searching = isSearching,
            cityCode = cityId,
            error = error
        ).also {
            _cityIdSearchState.postValue(it)
        }
    }

    private fun getCityIdFromRegions(regionsResult: RegionsResult): Long? {
        regionsResult.data.forEach { data ->
            data.cityId?.let {
                return it.toLong()
            }
        }

        return null
    }

    private fun saveHomeState(
        regionsResult: RegionsResult,
        cityId: Long,
        startDate: Calendar,
        endDate: Calendar,
        guestsCount: Int,
        roomsCount: Int
    ) {
        var city = "Unknown"
        var country = ""

        if (regionsResult.data.isNotEmpty()) {
            city = regionsResult.data[0].regionNames.shortName
            country = regionsResult.data[0].hierarchyInfo.country.name?:""
        }

        SearchFragmentUiModel(
            city,
            country,
            cityId,
            startDate,
            endDate,
            guestsCount,
            roomsCount
        ).also {
            searchFragmentState = it
        }
    }

    fun setNavigationCallback(callback: ((String) -> Unit)) {
        this.navigateToSearchResultCallback = callback
    }

    fun navigateToSearchViewResult(cityId: String) {
        if (cityId.isNotEmpty())
            navigateToSearchResultCallback(cityId)
    }

    fun updateHotels(
        cityId: Long,
        filter: FilterResult,
        checkInDate: Calendar,
        checkOutDate: Calendar,
        guestsCount: Int
    ) {
        getHotels(
            cityId, filter, checkInDate, checkOutDate, guestsCount,
            "WIFi", true
        )
        getHotels(
            cityId,
            filter,
            checkInDate,
            checkOutDate,
            guestsCount,
            "WIFI,PARKING,AIR_CONDITIONING,RESTAURANT_IN_HOTEL",
            false
        )
    }

    private fun getHotels(
        cityId: Long,
        filter: FilterResult,
        checkInDate: Calendar,
        checkOutDate: Calendar,
        guestsCount: Int,
        facilities: String,
        isRecommended: Boolean
    ) {
        Log.d("TAGH", "getHotels: Getting hotels")
        viewModelScope.launch {
            emitSearchResult(
                isRecommended = isRecommended,
                isLoading = true,
                appliedFilter = filter,
                cityId = cityId
            )

            safeApiCall {
                val res = hotelRepository.getHotels(
                    cityId,
                    "en_IN",
                    checkInDate.filterDate(),
                    filter.sortOrder.name,
                    guestsCount.toLong(),
                    "IN",
                    checkOutDate.filterDate(),
                    filter.priceRange.first.toLong(),
                    1,
                    filter.priceRange.second.toLong(),
                    facilities
                )
                Result.Success(res)
            }.also {
                if (it is Result.Error) {
                    emitSearchResult(
                        cityId = cityId,
                        appliedFilter = filter,
                        error = Event(resources.getString(R.string.no_internet)),
                        isRecommended = isRecommended
                    )
                    it.exception.printStackTrace()
                } else if (it is Result.Success) {
                    val res = it.data.body()

                    if (res != null) {
                        val data: List<Property> = res.properties

                        emitSearchResult(
                            isRecommended = isRecommended,
                            appliedFilter = filter,
                            result = data,
                            cityId = cityId
                        )
                    } else {
                        emitSearchResult(
                            error = Event(resources.getString(R.string.unknown_error_occurred)),
                            isRecommended = isRecommended,
                            appliedFilter = filter,
                            cityId = cityId
                        )
                    }
                }
            }
        }
    }

    fun Calendar.filterDate(): String {
        val day = this[Calendar.DAY_OF_MONTH]
        val month = this[Calendar.MONTH] + 1
        val year = this[Calendar.YEAR]

        val result = StringBuilder().apply {
            append(year)
            append('-')
            append(
                if (month > 9) month
                else "0$month"
            )
            append('-')
            append(
                if (day > 9) day
                else "0$day"
            )
        }

        return result.toString()
    }

    private fun emitSearchResult(
        isRecommended: Boolean,
        isLoading: Boolean = false,
        error: Event<String>? = null,
        appliedFilter: FilterResult,
        result: List<Property>? = null,
        cityId: Long
    ) {
        SearchResult(isLoading, error, appliedFilter, result, cityId)
            .also {
                if (isRecommended) {
                    _recommendedHotels.postValue(it)
                } else {
                    _businessHotels.postValue(it)
                }
            }
    }
}