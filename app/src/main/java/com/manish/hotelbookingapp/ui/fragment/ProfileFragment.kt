package com.manish.hotelbookingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.databinding.FragmentProfileBinding
import com.manish.hotelbookingapp.ui.activities.WelcomeActivity
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel

class ProfileFragment : Fragment() {
    private val homeViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = homeViewModel.getCurrentUser()

        if (user == null) {
            goToWelcomeActivity()
        } else {
            // Image
            val url = if (user.profileUrl.isNullOrEmpty())
                user.profileUrl
            else
                R.drawable.deault_user_ic

            Glide.with(requireContext())
                .load(url)
                .placeholder(R.color.place_holder_color)
                .into(binding.profileImage)

            // Name
            var name = user.name
            if (name.isNullOrEmpty())
                name = PreferenceHelper.getUserName()
            if (name.isNullOrEmpty())
                name = "Unknown"

            binding.txtName.text = name

            // Email
            binding.txtEmail.text = user.email ?: "Email is not known!"
        }

        binding.btnLogOut.setOnClickListener {
            homeViewModel.logOut()
            goToWelcomeActivity()
        }
    }

    private fun goToWelcomeActivity() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}