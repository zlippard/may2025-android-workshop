package com.zaclippard.androidworkshopapp.models

data class Principal(
    private val name: String,
    private val age: Int,
    val credentials: List<Credential>,
) : Person(name, age)
