package com.manish.hotelbookingapp.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.ActivityVerifyAccountBinding
import com.manish.hotelbookingapp.ui.sign_in.User
import com.manish.hotelbookingapp.ui.viewmodels.SignUpViewModel
import com.manish.hotelbookingapp.util.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyAccountBinding
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var email: String
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initializations
        email = intent.getStringExtra("email")!!
        userName = intent.getStringExtra("username")!!
        password = intent.getStringExtra("password")!!

        // Subtitle
        binding.txtSubtitle.text = resources.getString(
            R.string.please_enter_the_verification_code_sent_to_with_formatter,
            email
        )

        // Back button
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Verify
        binding.btnContinue.setOnClickListener {
            if (viewModel.verifyOtp(binding.otpContainer.otp)) {
                viewModel.createUserWithEmailAndPassword(userName, email, password)
            } else {
                Toast.makeText(this, resources.getString(R.string.wrong_otp), Toast.LENGTH_SHORT)
                    .show()
            }

            binding.otpContainer.setOTP("")
        }


        // Timer
        viewModel.timerState.observe(this) {
            val time = (it ?: 0) / 1000L

            var mint = (time / 60).toString()
            var sec = (time % 60).toString()

            if (mint.length < 2) {
                mint = "0$mint"
            }
            if (sec.length < 2) {
                sec = "0$sec"
            }

            binding.txtTimer.text = buildString {
                append(mint)
                append(":")
                append(sec)
            }

            // Refresh Resend button
            binding.otpResendBtn.isEnabled = (time == 0L)
            binding.otpResendBtn.setTextColor(
                getColor(
                    if (time == 0L)
                        R.color.button_text_color_2
                    else
                        R.color.button_text_color_2_disabled
                )
            )
        }

        // Send Email
        viewModel.generateAndSend(email)
        binding.otpResendBtn.setOnClickListener {
            viewModel.generateAndSend(email)
        }

        // Sign in state
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

    fun hideKeyboard(view: View?) {
        if (view == null) return
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}