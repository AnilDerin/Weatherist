package com.anilderin.weatherist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anilderin.weatherist.api.WeatherAPIService
import com.anilderin.weatherist.model.WeatherModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val weatherApiService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weatherData = MutableLiveData<WeatherModel>()
    val weatherLoading = MutableLiveData<Boolean>()
    val weatherError = MutableLiveData<Boolean>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {

        weatherLoading.value = true
        disposable.add(
            weatherApiService.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {
                    override fun onSuccess(t: WeatherModel) {
                        weatherData.value = t
                        weatherError.value = false
                        weatherLoading.value = false
                        Log.d(TAG, "onSuccess: Success")
                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        weatherLoading.value = false
                        Log.e(TAG, "onError: $e")
                    }
                })
        )
    }
}