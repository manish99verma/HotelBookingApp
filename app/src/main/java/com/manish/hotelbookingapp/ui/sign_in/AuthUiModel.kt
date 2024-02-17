package com.manish.hotelbookingapp.ui.sign_in

import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.MaterialDialogContent

data class AuthUiModel(
    val showProgress: Boolean,
    val authError: Event<AuthError>?,
    val success: Boolean,
//    val showAllLinkProvider: Event<Pair<List<String>, MaterialDialogContent>>?
)

data class AuthError(
    val authType: AuthType,
    val errorType: ErrorType?,
    val msgString: String?
)

enum class ErrorType {
    INTERNET,
    CANCELLED,
    INVALID_INPUT,
    WRONG_INPUT,
    USER_COLLISION
}