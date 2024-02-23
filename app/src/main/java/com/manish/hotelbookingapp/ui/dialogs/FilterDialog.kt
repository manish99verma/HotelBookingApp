package com.manish.hotelbookingapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.FiltersDialogBinding
import com.manish.hotelbookingapp.ui.models.FilterResult
import com.manish.hotelbookingapp.ui.models.Sort


class FilterDialog(
    val sortOrders: List<Sort>,
    val initialState: FilterResult,
    val onComplete: (FilterResult) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: FiltersDialogBinding
    private lateinit var ratingsData: List<Pair<LinearLayout, TextView>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FiltersDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SortOrder
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortOrders.map { it.value })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner2.adapter = adapter

        // Ratings
        ratingsData = listOf(
            Pair(binding.rl1, binding.txt1Star),
            Pair(binding.rl2, binding.txt2Star),
            Pair(binding.rl3, binding.txt3Star),
            Pair(binding.rl4, binding.txt4Star),
            Pair(binding.rl5, binding.txt5Star),
        )
    }

    private fun reset() {
        binding.spinner2.setSelection(sortOrders.indexOf(initialState.sortOrder))
    }

    private fun setRating(rating: Int) {
        ratingsData.forEachIndexed { i, curr ->
            curr.first.background = getDrawable(R.drawable.input_field_bg)
        }
    }

}