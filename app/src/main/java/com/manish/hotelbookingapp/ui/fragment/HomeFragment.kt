package com.manish.hotelbookingapp.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.FragmentHomeBinding
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel
import com.manish.hotelbookingapp.ui.dialogs.ProgressDialog
import java.util.Calendar
import java.util.Locale


class HomeFragment : Fragment() {
    private val homeViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private var checkInDate: Calendar? = null
    private var checkOutDate: Calendar? = null
    private var guestsCount = 0
    private var roomsCount = 0
    private var progressDialog: Dialog? = null
    private lateinit var context: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check In
        binding.calendarCheckIn.setOnClickListener {
//            showCalendar(true)
            showDatePickerDialog(true)
        }
        binding.rlCheckIn.setOnClickListener {
            showDatePickerDialog(true)
        }

        // Check Out
        binding.calendarCheckOut.setOnClickListener {
            showDatePickerDialog(false)
        }
        binding.rlChekOut.setOnClickListener {
            showDatePickerDialog(false)
        }

        // Guests
        setGuestsCount(DEFAULT_GUEST_COUNT)
        binding.plusGuests.setOnClickListener {
            setGuestsCount(guestsCount + 1)
        }
        binding.minusGuests.setOnClickListener {
            setGuestsCount(guestsCount - 1)
        }

        // Rooms
        setRoomsCount(DEFAULT_ROOMS_COUNT)
        binding.plusRooms.setOnClickListener {
            setRoomsCount(roomsCount + 1)
        }
        binding.minusRooms.setOnClickListener {
            setRoomsCount(roomsCount - 1)
        }

        // Search
        binding.btnContinue.setOnClickListener {
            validateInputAndSearch()
        }
        homeViewModel.citySearchResult.observe(context as LifecycleOwner) {
            Log.d("TAGH", "onViewCreated: res: $it")
            handleProgressDialog(it.searching)

            if (it.error != null) {
                it.error?.consume()?.let { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (it.cityCode != null) {
                it.cityCode.consume()?.let { id ->
                    Log.d("TAGH", "onViewCreated: CityCode: ${id}")

                    homeViewModel.navigateToSearchViewResult(id)
                }
            }
        }
    }

    private fun validateInputAndSearch() {
        var msg: String? = null
        if (binding.edtLocation.text.isEmpty()) {
            msg = "Please enter city or location!"
        } else if (checkInDate == null) {
            msg = "Please select your check in date!"
        } else if (checkOutDate == null) {
            msg = "Please select your check out date!"
        } else if (checkInDate!!.timeInMillis > checkOutDate!!.timeInMillis) {
            msg = "Invalid check out date!"
        }

        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                .show()
        } else {
            homeViewModel.getCityId(
                binding.edtLocation.text.toString(),
                checkInDate!!,
                checkOutDate!!,
                guestsCount,
                roomsCount
            )
        }
    }

    private fun handleProgressDialog(visible: Boolean) {
        if (visible) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
            }
            progressDialog!!.show()
        } else {
            progressDialog?.dismiss()
        }
    }

    private fun setGuestsCount(count: Int) {
        guestsCount = count

        if (guestsCount <= DEFAULT_GUEST_COUNT) {
            guestsCount = DEFAULT_GUEST_COUNT
            binding.minusGuests.isEnabled = false
            binding.minusGuests.alpha = 0.5f
        } else {
            binding.minusGuests.isEnabled = true
            binding.minusGuests.alpha = 1f
        }

        if (guestsCount >= MAX_GUEST_COUNT) {
            guestsCount = MAX_GUEST_COUNT
            binding.plusGuests.isEnabled = false
            binding.plusGuests.alpha = 0.5f
        } else {
            binding.plusGuests.isEnabled = true
            binding.plusGuests.alpha = 1f
        }

        binding.txtGuestsCount.text = guestsCount.toString()
    }

    private fun setRoomsCount(count: Int) {
        roomsCount = count

        if (roomsCount <= DEFAULT_GUEST_COUNT) {
            roomsCount = DEFAULT_GUEST_COUNT
            binding.minusRooms.isEnabled = false
            binding.minusRooms.alpha = 0.5f
        } else {
            binding.minusRooms.isEnabled = true
            binding.minusRooms.alpha = 1f
        }

        if (roomsCount >= MAX_ROOMS_COUNT) {
            roomsCount = MAX_ROOMS_COUNT
            binding.plusRooms.isEnabled = false
            binding.plusRooms.alpha = 0.5f
        } else {
            binding.plusRooms.isEnabled = true
            binding.plusRooms.alpha = 1f
        }

        binding.txtRoomsCount.text = roomsCount.toString()
    }

    private fun showDatePickerDialog(isCheckIn: Boolean) {
        val title = resources.getString(
            if (isCheckIn)
                R.string.check_in_dialog_title
            else
                R.string.check_out_dialog_title
        )

        val calendar = Calendar.getInstance()
        val y = calendar[Calendar.YEAR]
        val m = calendar[Calendar.MONTH]
        val d = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance(Locale.getDefault())
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                cal[Calendar.MONTH] = month
                cal[Calendar.YEAR] = year

                val msg = buildString {
                    append(dayOfMonth)
                    append('/')
                    append(
                        if ((month + 1) < 10)
                            "0${month + 1}"
                        else
                            month + 1
                    )
                    append('/')
                    append(year)
                }

                if (isCheckIn) {
                    binding.txtCheckIn.text = msg
                    checkInDate = cal
                } else {
                    binding.txtCheckOut.text = msg
                    checkOutDate = cal
                }
            },
            y, m, d
        )

        // Constraints
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        val temp = Calendar.getInstance()
        temp[Calendar.MONTH] = Calendar.DECEMBER
        datePickerDialog.datePicker.maxDate = temp.timeInMillis
        datePickerDialog.show()
    }

    companion object {
        private const val DEFAULT_GUEST_COUNT = 1
        private const val MAX_GUEST_COUNT = 20
        private const val DEFAULT_ROOMS_COUNT = 1
        private const val MAX_ROOMS_COUNT = 20
    }
}