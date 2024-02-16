package com.manish.hotelbookingapp.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.ui.sign_in.AuthType
import com.manish.hotelbookingapp.databinding.ActivitySignUpBinding
import com.manish.hotelbookingapp.ui.sign_in.User
import com.manish.hotelbookingapp.ui.viewmodels.SignUpViewModel
import com.manish.hotelbookingapp.ui.viewmodels.SignUpViewModel.Companion.RC_GOOGLE_SIGN_IN_CODE
import com.manish.hotelbookingapp.util.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Password show btn
        var isVisible = false
        binding.inputFieldPassword
            .apply {
                setEndIconOnClickListener {
                    if (isVisible) {
                        isVisible = false
                        setEndIconDrawable(R.drawable.ic_hide)
                        editText!!.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    } else {
                        isVisible = true
                        setEndIconDrawable(R.drawable.ic_view)
                        editText!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    }

                    editText!!.setSelection(editText!!.length())
                }
            }

        // SignUp
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, VerifyAccount::class.java)
            startActivity(intent)
        }

        // Google Sign in
        binding.otherLoginOptions.btnLoginWithGoogle.setOnClickListener {
            doSocialAuth(AuthType.GOOGLE)
        }

        // Facebook Sign in
        binding.otherLoginOptions.btnLoginWithFb.setOnClickListener {
            doSocialAuth(AuthType.FACEBOOK)
        }


        viewModel.uiState.observe(this) { authModel ->
            handleProgressDialog(authModel.showProgress)

            if (authModel.success) navigateToOtherActivity(viewModel.getCurrUser())
            else if (authModel.error != null && !authModel.error.consumed)
                authModel.error.consume()?.let { pair ->
                    Log.d("TAGF", "onCreate: Login Error: $pair")
                }
            else if (authModel.showAllLinkProvider != null && !authModel.showAllLinkProvider.consumed)
                authModel.showAllLinkProvider.consume()?.let { pair ->
                    Log.d("TAGF", "onCreate: $pair")
                }
        }
    }

    private fun handleProgressDialog(visible: Boolean) {
        if (visible) {
            if (progressDialog == null){
                progressDialog = ProgressDialog(this)
            }
            progressDialog!!.show()
        } else {
            progressDialog?.let {
                it.dismiss()
            }
        }
    }

    private fun navigateToOtherActivity(user: User?) {
        Log.d("TAGF", "navigateToOtherActivity: $user")
    }

    private fun doSocialAuth(authType: AuthType) {
        when (authType) {
            AuthType.GOOGLE -> viewModel.googleSignIn().also {
                googleCallback.launch(it)
            }

            AuthType.FACEBOOK -> {
                viewModel.loginManager.logInWithReadPermissions(
                    this, { requestCode, resultCode, data ->
                        viewModel.handleOnActivityResult(requestCode, resultCode, data)
                        true
                    }, SignUpViewModel.facebook_permissions
                )
            }

            AuthType.EMAIL -> {
                viewModel.fetchAllProviderForEmail(authType.authValue)
            }

            else -> {}
        }
    }

    private val googleCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.handleOnActivityResult(RC_GOOGLE_SIGN_IN_CODE, result.resultCode, result.data)
        }
}