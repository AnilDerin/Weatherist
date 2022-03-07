package com.anilderin.weatherist.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anilderin.weatherist.databinding.ActivityMainBinding
import com.anilderin.weatherist.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val cName = GET.getString("cityName", "Istanbul")
        edt_city_name.setText(cName)
        viewModel.refreshData(cName!!)

        getLiveData()

        binding.apply {

            swipeRefreshLayout.setOnRefreshListener {
                ll_data.visibility = View.GONE
                tv_error.visibility = View.GONE
                pb_loading.visibility = View.GONE

                val cityName = GET.getString("cityName", cName)
                edt_city_name.setText(cityName)
                viewModel.refreshData(cityName!!)
                swipe_refresh_layout.isRefreshing = false
            }
            img_search_city.setOnClickListener {
                val cityName = edt_city_name.text.toString()
                SET.putString("cityName", cityName)
                SET.apply()
                viewModel.refreshData(cityName)
                getLiveData()
                Log.i(TAG, "onCreate: $cityName")
                it.hideKeyboard()
            }
        }
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    @SuppressLint("SetTextI18n")
    private fun getLiveData() {

        viewModel.weatherData.observe(this, Observer { data ->
            data?.let {
                ll_data.visibility = View.VISIBLE

                tv_city_code.text = data.sys.country
                tv_city_name.text = data.name

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
                    .into(img_weather_pictures)

                tv_degree.text = data.main.temp.toInt().toString() + "°C"
                tv_humidity.text = data.main.humidity.toString() + "%"
                tv_wind_speed.text = data.wind.speed.toString() + " km/h"
                tv_lat.text = data.coord.lat.toString()
                tv_lon.text = data.coord.lon.toString()
                tv_description.text = data.weather[0].description.capitalize()

            }
        })

        viewModel.weatherError.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    tv_error.visibility = View.VISIBLE
                    pb_loading.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    tv_error.visibility = View.GONE
                }
            }
        })

        viewModel.weatherLoading.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }
            }
        })
    }
}