package com.manish.hotelbookingapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.regions.RegionsResult
import com.manish.hotelbookingapp.data.web_server.HotelRepository
import com.manish.hotelbookingapp.data.web_server.HotelRepositoryInterface
import com.manish.hotelbookingapp.ui.models.GetCityUiModel
import com.manish.hotelbookingapp.ui.models.SearchFragmentUiModel
import com.manish.hotelbookingapp.ui.sign_in.User
import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.Result
import com.manish.hotelbookingapp.util.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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
    val citySearchResult: LiveData<GetCityUiModel> get() = _cityIdSearchState
    var searchFragmentState: SearchFragmentUiModel? = null
        private set

    private lateinit var navigateToSearchResultCallback: ((String) -> Unit)

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
                    } else
                        emitCitySearchState(
                            error = Event(resources.getString(R.string.unknown_error_occurred))
                        )
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
        var country = "Unknown"

        if (regionsResult.data.isNotEmpty()) {
            city = regionsResult.data[0].regionNames.shortName
            country = regionsResult.data[0].hierarchyInfo.country.name
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
}