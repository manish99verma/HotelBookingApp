package com.manish.hotelbookingapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.FiltersDialogBinding

class FilterDialog(onCompleteListener:) : BottomSheetDialogFragment() {
    private lateinit var binding: FiltersDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FiltersDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}