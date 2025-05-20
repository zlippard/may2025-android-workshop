package com.zaclippard.androidworkshopapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryFlagDto(
    val png: String,
)
