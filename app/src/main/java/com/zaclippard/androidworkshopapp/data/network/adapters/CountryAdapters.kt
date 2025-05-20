package com.zaclippard.androidworkshopapp.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import com.zaclippard.androidworkshopapp.data.network.dto.CountryDto
import com.zaclippard.androidworkshopapp.data.network.dto.CountryNameDto
import com.zaclippard.androidworkshopapp.domain.Country

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class WrappedCountryList

class CountryAdapter {
    @WrappedCountryList
    @FromJson
    fun fromJson(countryDtoList: List<CountryDto>) : List<Country> = countryDtoList.map { countryDto ->
        Country(
            name = countryDto.name.common,
            capital = countryDto.capital?.firstOrNull() ?: "N/A",
        )
    }

    @ToJson
    fun toJson(@WrappedCountryList countryList: List<Country>): List<CountryDto> = countryList.map { country ->
        CountryDto(
            name = CountryNameDto(common = country.name),
            capital = listOf(country.capital),
        )
    }
}
