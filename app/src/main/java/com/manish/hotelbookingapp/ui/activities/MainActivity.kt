package com.manish.hotelbookingapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.manish.hotelbookingapp.BuildConfig
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.data.local_database.PreferenceHelper
import com.manish.hotelbookingapp.data.web_server.HotelsApiService
import com.manish.hotelbookingapp.databinding.ActivityMainBinding
import com.manish.hotelbookingapp.ui.fragment.FavoritesFragment
import com.manish.hotelbookingapp.ui.fragment.HomeFragment
import com.manish.hotelbookingapp.ui.fragment.MyBookingsFragment
import com.manish.hotelbookingapp.ui.fragment.ProfileFragment
import com.manish.hotelbookingapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fragmentsMap: Map<FragmentKey, FragmentData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceHelper.initialize(applicationContext)

        // Check User
        if (!viewModel.isAuthenticated()) {
            val welcomeIntent = Intent(this, WelcomeActivity::class.java)
            startActivity(welcomeIntent)
            finish()
        }

        // Fragments
        fragmentsMap = mapOf(
            FragmentKey.HOME to FragmentData(
                binding.imageHome,
                binding.txtHome,
                R.drawable.home,
                R.drawable.home_selected
            ),
            FragmentKey.FAVORITES to FragmentData(
                binding.imageFavorites,
                binding.txtFavorites,
                R.drawable.favorites,
                R.drawable.favorites_selected
            ),
            FragmentKey.BOOKINGS to FragmentData(
                binding.imageBooking,
                binding.txtBooking,
                R.drawable.bookings,
                R.drawable.bookings_selected
            ),
            FragmentKey.PROFILE to FragmentData(
                binding.imageProfile,
                binding.txtProfile,
                R.drawable.profile,
                R.drawable.profile_selected
            )
        )

        binding.layoutHome.setOnClickListener {
            selectFragment(FragmentKey.HOME)
        }
        binding.layoutBookings.setOnClickListener {
            selectFragment(FragmentKey.BOOKINGS)
        }
        binding.layoutFavorites.setOnClickListener {
            selectFragment(FragmentKey.FAVORITES)
        }
        binding.layoutProfile.setOnClickListener {
            selectFragment(FragmentKey.PROFILE)
        }

        selectFragment(FragmentKey.HOME)
    }

    private fun test() {
        lifecycleScope.launch(IO) {
            Log.d("TAGH", "test: Sending request")
            val httpClient = OkHttpClient.Builder().apply {
                addInterceptor(HeaderInterceptor())
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOTELS_API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(HotelsApiService::class.java)

//            val response = service.getHotels(
//                3000406907L,
//                "en_IN",
//                "2024-02-25",
//                "RECOMMENDED",
//                1,
//                "IN",
//                "2024-02-28",
//                0L,
//                1,
//                1000000L,
//                "SPA_ON_SITE"
//            )
//            Log.d("TAGH", "test: Retrofit: ${response.body()}")

//            val jObjError = JSONObject(
//                response.errorBody()!!.string()
//            )
//            Log.d("TAGH", "test: Retrofit: $jObjError")

//            val response = service.getHotelDetails("IN", 83141779L, "en_IN")
//            Log.d("TAGH", "test: ${response.body()}")
//
//            val jObjError = JSONObject(
//                response.errorBody()!!.string()
//            )
//            Log.d("TAGH", "test: $jObjError")

        }
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest: Request = chain.request()
            val requestBuilder: Request.Builder = originalRequest.newBuilder()
                .header("X-RapidAPI-Key", BuildConfig.X_API_KEY)
                .header("X-RapidAPI-Host", BuildConfig.X_API_HOST)
            val request: Request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    private fun selectFragment(type: FragmentKey) {
        var fragment = fragmentsMap[type]!!.fragment
        if (fragment == null) {
            fragment = when (type) {
                FragmentKey.HOME -> HomeFragment()
                FragmentKey.BOOKINGS -> MyBookingsFragment()
                FragmentKey.FAVORITES -> FavoritesFragment()
                else -> ProfileFragment()
            }

            fragmentsMap[type]!!.fragment = fragment
        }

        // Replace the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        // Focus Selected
        fragmentsMap.forEach { currType, fData ->
            if (currType == type) {
                fData.icon.setImageResource(fData.selectedIcon)
                fData.text.setTextColor(getColor(R.color.surface_color_1))
                fData.text.setTextAppearance(R.style.txt_navigation_title_selected)
            } else {
                fData.icon.setImageResource(fData.normalIcon)
                fData.text.setTextColor(getColor(R.color.txt_navigation_txt))
                fData.text.setTextAppearance(R.style.txt_navigation_title)
            }
        }
        test()
    }

    private enum class FragmentKey {
        HOME,
        FAVORITES,
        BOOKINGS,
        PROFILE
    }

    private data class FragmentData(
        val icon: ImageView,
        val text: TextView,
        val normalIcon: Int,
        val selectedIcon: Int,
        var fragment: Fragment? = null
    )
}