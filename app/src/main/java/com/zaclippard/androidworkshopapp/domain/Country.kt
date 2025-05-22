package com.zaclippard.androidworkshopapp.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "countries")
data class Country(
    @PrimaryKey
    val name: String,
    val capital: String,
    val flagUrl: String,
    val population: Long,
    val area: Float,
    val isFavorite: Boolean = false,
) : Parcelable
