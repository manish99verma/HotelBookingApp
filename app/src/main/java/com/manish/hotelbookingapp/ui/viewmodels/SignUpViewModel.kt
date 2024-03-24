package com.manish.hotelbookingapp.ui.viewmodels

import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.manish.hotelbookingapp.BuildConfig
import com.manish.hotelbookingapp.HotelBookingApp
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.ui.sign_in.AuthError
import com.manish.hotelbookingapp.ui.sign_in.AuthType
import com.manish.hotelbookingapp.ui.sign_in.AuthUiModel
import com.manish.hotelbookingapp.ui.sign_in.ErrorType
import com.manish.hotelbookingapp.ui.sign_in.User
import com.manish.hotelbookingapp.util.Event
import com.manish.hotelbookingapp.util.Result
import com.manish.hotelbookingapp.util.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import papaya.`in`.sendmail.SendMail
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextLong

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: HotelBookingApp,
    private val firebaseAuth: FirebaseAuth
) : AndroidViewModel(application) {
    val uiState: LiveData<AuthUiModel> get() = _uiState

    private val resources = application.resources
    private val _uiState = MutableLiveData<AuthUiModel>()

    companion object {
        const val RC_GOOGLE_SIGN_IN_CODE = 2555
        val facebook_permissions = listOf("email", "public_profile")
        const val MINIMUM_USERNAME_LENGTH = 3
        const val MAXIMUM_USERNAME_LENGTH = 25
        const val MINIMUM_PASSWORD_LENGTH = 6
        const val MAXIMUM_PASSWORD_LENGTH = 25
    }

    fun getCurrUser(): User? {
        return firebaseAuth.currentUser?.let {
            User(it.uid, it.displayName, it.email, it.photoUrl.toString())
        }
    }

    /**
     * Fetch all providers associated with this email. In case when user previously login with xyz provider and after logout he/she try to login with abc provider.
     */

    fun fetchAllProviderForEmail(email: String) {
        viewModelScope.launch {
            safeApiCall {
                Result.Success(
                    firebaseAuth.fetchSignInMethodsForEmail(
                        email
                    ).await()
                )
            }.also {
                if (it is Result.Success && it.data.signInMethods != null)
                    sendErrorState(
                        AuthType.EMAIL,
                        ErrorType.USER_COLLISION,
                        String.format(
                            resources.getString(R.string.auth_user_collision_message),
                            email
                        )
                    )
                else sendErrorState(AuthType.EMAIL, ErrorType.INTERNET)
            }
        }
    }

    private suspend fun sendErrorState(
        authType: AuthType,
        errorType: ErrorType? = null,
        msg: String? = null
    ) {
        emitUiState(
            error = Event(
                AuthError(authType, errorType, msg)
            )
        )
    }


    //////////////////////// Firebase Authentication Common Code Starts ////////////////////////////
    private suspend fun handleErrorStateForSignInCredential(
        exception: Exception, authType: AuthType
    ) {
        if (exception is FirebaseAuthUserCollisionException) {
            val email = exception.email
            if (email != null) fetchAllProviderForEmail(email)
            else sendErrorState(authType)
        } else sendErrorState(authType)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE && data != null) {
            handleGoogleSignInResult(data)
        } else if (mCallbackManager.onActivityResult(requestCode, resultCode, data))
            println("Result should be handled")
    }

    @Throws(Exception::class)
    private suspend fun signInWithCredential(authCredential: AuthCredential): AuthResult? {
        return firebaseAuth.signInWithCredential(authCredential).await()
    }

    private suspend fun emitUiState(
        showProgress: Boolean = false,
        error: Event<AuthError>? = null,
        success: Boolean = false,
    ) = withContext(Dispatchers.Main)
    {
        AuthUiModel(showProgress, error, success).also {
            _uiState.value = it
        }
    }

    //////////////////////// Firebase Authentication Common Code Ends //////////////////////////////


    //////////////////////// Firebase Google Authentication Starts /////////////////////////////////

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(resources.getString(R.string.google_web_client))
        .requestId()
        .requestProfile()
        .requestEmail()
        .build()
    private val mGoogleSignClient by lazy {
        GoogleSignIn.getClient(application, gso)
    }

    private fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            safeApiCall {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
                val authResult =
                    signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))!!
                Result.Success(authResult)
            }.also {
                if (it is Result.Success && it.data.user != null)
                    emitUiState(success = true)
                else sendErrorState(AuthType.GOOGLE)
            }
        }
    }

    fun googleSignIn() = mGoogleSignClient.signInIntent

    //////////////////////// Firebase Google Authentication Ends ///////////////////////////////////

    /////////////////////////// Firebase Facebook Authentication Starts ////////////////////////////

    val loginManager: LoginManager = LoginManager.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val mFacebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
            handleFacebookCredential(credential)
        }

        override fun onCancel() {
            viewModelScope.launch {
                sendErrorState(
                    AuthType.FACEBOOK,
                    ErrorType.CANCELLED,
                    resources.getString(R.string.operation_cancelled_content)
                )
            }
        }

        override fun onError(error: FacebookException) {
            viewModelScope.launch {
                sendErrorState(AuthType.FACEBOOK)
            }
        }
    }

    init {
        loginManager.registerCallback(mCallbackManager, mFacebookCallback)
    }

    private fun handleFacebookCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            safeApiCall { Result.Success(signInWithCredential(authCredential)!!) }.also {
                if (it is Result.Success && it.data.user != null) emitUiState(success = true)
                else if (it is Result.Error) handleErrorStateForSignInCredential(
                    it.exception, AuthType.FACEBOOK
                )
            }
        }
    }

    //////////////////////// Firebase Facebook Authentication Ends /////////////////////////////////


    //////////////////////// Firebase Email Authentication Starts /////////////////////////////////
    private var currOtp: Long = -1
    private var resendButtonTime = 60 * 1000L
    private val _timerState = MutableLiveData(0L)
    val timerState: LiveData<Long> get() = _timerState
    private var timer: Timer? = null

    fun createUserWithEmailAndPassword(userName: String, email: String, password: String) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            val inputError = validateInputs(userName, email, password)
            if (inputError != null) {
                sendErrorState(AuthType.EMAIL, ErrorType.INVALID_INPUT, inputError)
                return@launch
            }

            safeApiCall {
                val response =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Result.Success(response!!)
            }.also {
                if (it is Result.Success && it.data.user != null) {
                    PreferenceHelper.saveUserName(userName)
                    emitUiState(success = true)
                } else if (it is Result.Error)
                    sendErrorState(AuthType.EMAIL, errorType = null, it.exception.localizedMessage)
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            val inputError = validateInputs(email = email, password = password)
            if (inputError != null) {
                sendErrorState(AuthType.EMAIL, ErrorType.INVALID_INPUT, inputError)
                return@launch
            }

            safeApiCall {
                val response =
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Result.Success(response!!)
            }.also {
                if (it is Result.Success && it.data.user != null) {
                    emitUiState(success = true)
                } else if (it is Result.Error && it.exception.message != null)
                    sendErrorState(
                        AuthType.EMAIL,
                        ErrorType.WRONG_INPUT,
                        resources.getString(R.string.invalid_input_in_sign_in)
                    )
            }
        }
    }

    fun validateInputs(userName: String? = null, email: String, password: String): String? {
        // Check Empty
        if (userName != null && userName.isEmpty())
            return String.format(
                resources.getString(
                    R.string.please_enter_your_inputType,
                    resources.getString(R.string.username)
                )
            )

        if (email.isEmpty())
            return String.format(
                resources.getString(
                    R.string.please_enter_your_inputType,
                    resources.getString(R.string.email)
                )
            )

        if (password.isEmpty())
            return resources.getString(
                R.string.please_enter_your_inputType, resources.getString(R.string.password)
            )

        // Check validity & length
        if (userName != null && userName.length < MINIMUM_USERNAME_LENGTH)
            return resources.getString(
                R.string.please_enter_a_larger_inputType,
                resources.getString(R.string.username)
            )

        if (userName != null && userName.length > MAXIMUM_USERNAME_LENGTH)
            return resources.getString(
                R.string.please_enter_a_smaller_inputType,
                resources.getString(R.string.username)
            )

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return resources.getString(
                R.string.please_enter_a_valid_email
            )


        if (password.length < MINIMUM_PASSWORD_LENGTH)
            return resources.getString(
                R.string.please_enter_a_larger_inputType,
                resources.getString(R.string.password)
            )

        if (password.length > MAXIMUM_PASSWORD_LENGTH)
            return resources.getString(
                R.string.please_enter_a_smaller_inputType,
                resources.getString(R.string.password)
            )

        return null
    }

    fun generateAndSend(userName:String, email: String) {
        Log.d("TAGF", "generateAndSend: ")

        timer?.cancel()
        currOtp = Random.nextLong(100000L..999999L)
        val mail = SendMail(
            BuildConfig.EMAIL_SEND_EMAIL,
            BuildConfig.EMAIL_SEND_PASS_KEY,
            email,
            "Welcome to WanderStay - Your OTP Confirmation",
            "Dear $userName,\n" +
                    "\n" +
                    "Welcome to Wanderstay! We're thrilled to have you join our community of travelers.\n" +
                    "\n" +
                    "As a part of our secure signup process, we have generated an OTP (One-Time Password) for your account verification. Your OTP is: $currOtp.\n" +
                    "\n" +
                    "Please use this OTP to complete your registration process and unlock all the benefits of being a Wanderstay member.\n" +
                    "\n" +
                    "If you have any questions or need assistance, don't hesitate to reach out to our support team at [Your Contact Information]. We're here to help!\n" +
                    "\n" +
                    "Thank you for choosing Wanderstay. We look forward to being a part of your travel adventures.\n" +
                    "\n" +
                    "Best regards,\n" +
                    "\n" +
                    "Wanderstay"
        )

        mail.execute()

        // Disable resend button
        _timerState.postValue(resendButtonTime)
        resendButtonTime *= 2

        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val timeLeft = (_timerState.value ?: 0) - 1000L
                if (timeLeft > 0) {
                    _timerState.postValue(timeLeft)
                } else {
                    timer?.cancel()
                    _timerState.postValue(0L)
                }
            }
        }, 1000, 1000)
    }

    fun verifyOtp(otp: String?): Boolean {
        return try {
            otp!!.toLong() == currOtp
        } catch (e: Exception) {
            false
        }
    }

    //////////////////////// Firebase Email Authentication Ends /////////////////////////////////
}