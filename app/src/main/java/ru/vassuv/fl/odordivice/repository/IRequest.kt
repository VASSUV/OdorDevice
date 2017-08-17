package ru.vassuv.fl.odordivice.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRequest {
    @GET("v0/")
    fun get(@Query("test.pullOdorDeviceStatistic", encoded = true) json : String): Call<String>
}
