package com.zaclippard.androidworkshopapp.domain

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Country(
    val name: String,
    val capital: String,
) : Parcelable
