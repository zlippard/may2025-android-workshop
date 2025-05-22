package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsScreen(
    countryIndex: Int,
    viewModel: CountryDetailsViewModel = viewModel(
        factory = CountryDetailsViewModel.createFactory(countryIndex)
    ),
    onNavigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(uiState.title)
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
        Box(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
            when (val state = uiState) {
                is CountryDetailsUiState.Ready -> CountryDetails(state.country)
                is CountryDetailsUiState.Error -> Text(state.message)
            }
        }
    }
}

@Composable
private fun CountryDetails(country: Country) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier.height(40.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(country.flagUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.country_flag_content_description),
        )

        Column {
            Text(stringResource(R.string.country_capital, country.name))
            Text(stringResource(R.string.country_population, country.population))
            Text(stringResource(R.string.country_area, country.area))
        }
    }
}
