package com.zaclippard.androidworkshopapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryNameDto(
    val common: String,
)
