package com.zaclippard.androidworkshopapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDto(
    val name: CountryNameDto,
    val capital: List<String>?,
    val flags: CountryFlagDto,
    val population: Long,
    val area: Float,
)

