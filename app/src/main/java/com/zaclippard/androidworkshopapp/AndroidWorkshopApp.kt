package com.zaclippard.androidworkshopapp

import android.app.Application
import com.squareup.moshi.Moshi
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.data.network.adapters.CountryAdapter
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AndroidWorkshopApp : Application() {
    private val moshi = Moshi.Builder()
        .add(CountryAdapter())
        .build()

    private lateinit var retrofit: Retrofit

    lateinit var countryService: CountryService
    lateinit var countryRepository: CountryRepository

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        countryService = retrofit.create(CountryService::class.java)
        countryRepository = CountryRepositoryImpl(countryService)
    }
}
