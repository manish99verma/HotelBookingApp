package com.manish.hotelbookingapp.ui.sign_in

import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.MaterialDialogContent

data class AuthUiModel(
    val showProgress: Boolean,
    val error: Event<Pair<AuthType, MaterialDialogContent>>?,
    val success: Boolean,
    val showAllLinkProvider: Event<Pair<List<String>, MaterialDialogContent>>?
)