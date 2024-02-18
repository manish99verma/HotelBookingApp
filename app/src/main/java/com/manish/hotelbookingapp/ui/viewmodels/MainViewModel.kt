package com.manish.hotelbookingapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.manish.hotelbookingapp.data.PreferenceHelper
import com.manish.hotelbookingapp.ui.sign_in.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val auth: FirebaseAuth) :
    AndroidViewModel(application) {

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): User? {
        val user = auth.currentUser ?: return null
        return User(user.uid, user.displayName, user.email, user.photoUrl.toString())
    }

    fun logOut(){
        auth.signOut()
    }

}