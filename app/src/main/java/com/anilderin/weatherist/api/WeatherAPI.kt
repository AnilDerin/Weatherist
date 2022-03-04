package com.anilderin.weatherist.api

import com.anilderin.weatherist.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&APPID=636bad42860fd6f45dc2263d47f80435")
    fun getData(
        @Query("q") cityName: String,
    ): Single<WeatherModel>

}