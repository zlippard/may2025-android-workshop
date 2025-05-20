package com.zaclippard.androidworkshopapp.data.network

import com.zaclippard.androidworkshopapp.data.network.adapters.WrappedCountryList
import com.zaclippard.androidworkshopapp.domain.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
    @GET("v3.1/all")
    @WrappedCountryList
    suspend fun getAllCountries(): Response<List<Country>>
}
