package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsScreen(
    country: Country,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(country.name)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back_content_description),
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Name: ${country.name}")
            Text("Capital: ${country.capital}")
        }
    }
}
