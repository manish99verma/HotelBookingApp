package com.manish.hotelbookingapp.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.ActivitySignUpBinding
import com.manish.hotelbookingapp.ui.sign_in.AuthType
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
            val userName = binding.edtUserName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val passWord = binding.edtPassword.text.toString()

            val validityErrorMsg = viewModel.validateInputs(
                userName, email, passWord
            )

            if (validityErrorMsg != null) {
                Toast.makeText(this, validityErrorMsg, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, VerifyAccountActivity::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("email", email)
                intent.putExtra("password", passWord)
                startActivity(intent)
            }
        }

        // Google Sign in
        binding.otherLoginOptions.btnLoginWithGoogle.setOnClickListener {
            doSocialAuth(AuthType.GOOGLE)
        }

        // Facebook Sign in
        binding.otherLoginOptions.btnLoginWithFb.setOnClickListener {
            doSocialAuth(AuthType.FACEBOOK)
        }

        // Sign up State
        viewModel.uiState.observe(this) { authModel ->
            handleProgressDialog(authModel.showProgress)

            if (authModel.success) {
                navigateToOtherActivity()
            } else if (authModel.authError != null && !authModel.authError.consumed) {
                authModel.authError.consume()?.let { err ->
                    Toast.makeText(
                        this,
                        err.msgString ?: resources.getString(R.string.unknown_error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Back button
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleProgressDialog(visible: Boolean) {
        if (visible) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(this)
            }
            progressDialog!!.show()
        } else {
            progressDialog?.let {
                it.dismiss()
            }
        }
    }

    private fun navigateToOtherActivity() {
        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
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