package com.manish.hotelbookingapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.manish.hotelbookingapp.ui.sign_in.User
import dagger.hilt.android.lifecycle.HiltViewModel
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