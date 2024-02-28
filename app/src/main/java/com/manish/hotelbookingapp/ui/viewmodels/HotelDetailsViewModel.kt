package com.manish.hotelbookingapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.model.hotel_details.HotelDetailsResult
import com.manish.hotelbookingapp.data.web_server.HotelRepositoryInterface
import com.manish.hotelbookingapp.ui.models.HotelDetailsUIModel
import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.Result
import com.manish.hotelbookingapp.util.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    application: Application,
    private val auth: FirebaseAuth,
    private val hotelRepository: HotelRepositoryInterface
) : AndroidViewModel(application) {
    private val resources = application.resources
    private val _hotelDetailsLiveData = MutableLiveData<HotelDetailsUIModel>()
    val hotelDetailsData: LiveData<HotelDetailsUIModel> get() = _hotelDetailsLiveData

    fun getHotelDetails(hotelId: String) {
        viewModelScope.launch {
            emitHotelDetails(hotelId, true)
            Log.d("TAGH", "getHotelDetails: id $hotelId")

            safeApiCall {
                val res = hotelRepository.getHotelDetails("IN", hotelId, "en_IN")
                Result.Success(res)
            }.also {
                if (it is Result.Error) {
                    it.exception.printStackTrace()
                    emitHotelDetails(
                        hotelId = hotelId,
                        error = Event(resources.getString(R.string.no_internet))
                    )
                } else if (it is Result.Success && !it.data.isSuccessful) {
                    Log.d("TAGH", "getHotelDetails: ${it.data.code()}")
                    emitHotelDetails(
                        hotelId = hotelId,
                        error = Event(resources.getString(R.string.unknown_error_occurred))
                    )
                } else if (it is Result.Success) {
                    emitHotelDetails(hotelId = hotelId, data = it.data.body())
                }
            }

        }
    }

    fun emitHotelDetails(
        hotelId: String,
        isLoading: Boolean = false,
        error: Event<String>? = null,
        data: HotelDetailsResult? = null
    ) {
        HotelDetailsUIModel(hotelId, isLoading, error, data)
            .also {
                _hotelDetailsLiveData.postValue(it)
            }
    }

}