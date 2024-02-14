package com.manish.hotelbookingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manish.hotelbookingapp.data.PreferenceHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun isFirstSession() = true
//        (PreferenceHelper.getCurrSession() == 1)

}