package com.manish.hotelbookingapp.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.FiltersDialogBinding
import com.manish.hotelbookingapp.ui.models.FilterResult
import com.manish.hotelbookingapp.ui.models.Sort


class FilterDialog(
    private val sortOrders: List<Sort>,
    private val initialState: FilterResult,
    private val onComplete: (FilterResult) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: FiltersDialogBinding
    private lateinit var ratingsData: List<Pair<LinearLayout, TextView>>
    private lateinit var context: Context
    private var currRating = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FiltersDialogBinding.inflate(layoutInflater, container, false)
        context = binding.root.context
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SortOrder
        val adapter: ArrayAdapter<String> =
            CustomSpinnerAdapter(context, sortOrders.map { it.value })
        binding.spinner2.adapter = adapter

        // Ratings
        ratingsData = listOf(
            Pair(binding.rl1, binding.txt1Star),
            Pair(binding.rl2, binding.txt2Star),
            Pair(binding.rl3, binding.txt3Star),
            Pair(binding.rl4, binding.txt4Star),
            Pair(binding.rl5, binding.txt5Star),
        )
        binding.rl1.setOnClickListener {
            setRating(1)
        }
        binding.rl2.setOnClickListener {
            setRating(2)
        }
        binding.rl3.setOnClickListener {
            setRating(3)
        }
        binding.rl4.setOnClickListener {
            setRating(4)
        }
        binding.rl5.setOnClickListener {
            setRating(5)
        }

        // Slider
        binding.rangeSeekBar.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
                setSliderValue(slider.values[0].toInt(), slider.values[1].toInt())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                setSliderValue(slider.values[0].toInt(), slider.values[1].toInt())
            }
        })

        // Apply
        binding.btnApply.setOnClickListener {
            onComplete(
                FilterResult(
                    sortOrders[binding.spinner2.selectedItemPosition],
                    currRating,
                    Pair(
                        binding.rangeSeekBar.values[0].toInt(),
                        binding.rangeSeekBar.values[1].toInt()
                    )
                )
            )

            dismiss()
        }

        // Reset
        binding.resetBtn.setOnClickListener {
            reset()
        }
        reset()

        // Cancel
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun reset() {
        binding.spinner2.setSelection(sortOrders.indexOf(initialState.sortOrder))
        setRating(initialState.rating)

        setSliderValue(initialState.priceRange.first, initialState.priceRange.second)
        binding.rangeSeekBar.values = listOf(
            initialState.priceRange.first.toFloat(),
            initialState.priceRange.second.toFloat()
        )
    }

    private fun setSliderValue(start: Int, end: Int) {
        binding.txtPriceRangeResult.text = buildString {
            append("Rs")
            append(start)
            append(" - ")
            append("Rs")
            append(end)
        }
    }

    private fun setRating(rating: Int) {
        ratingsData.forEach { curr ->
            curr.first.background = getDrawable(context, R.drawable.input_field_bg)
            curr.second.setTextColor(context.getColor(R.color.txt_navigation_txt))
        }

        ratingsData[rating - 1].first.background =
            getDrawable(context, R.drawable.rating_bg_seleced)
        ratingsData[rating - 1].second.setTextColor(context.getColor(R.color.white))
        currRating = rating
    }


    class CustomSpinnerAdapter(
        context: Context,
        private val items: List<String>
    ) : ArrayAdapter<String>(context, R.layout.item_spinner_not_selected, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createItemView(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createItemView(position, convertView, parent)
        }

        private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_spinner_not_selected, parent, false)
            val textView = view.findViewById<TextView>(R.id.textView)
            textView.text = items[position]
            return view
        }
    }
}