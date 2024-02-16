package com.manish.hotelbookingapp.util

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.manish.hotelbookingapp.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        val attrs = window?.attributes

        attrs?.gravity = Gravity.CENTER_HORIZONTAL
        window?.attributes = attrs

        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)

        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null, false)
        setContentView(view)
    }

}