package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.components.RetryableError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    viewModel: CountryListViewModel = viewModel(factory = CountryListViewModel.Factory),
    onCountryClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.country_title))
                },
                navigationIcon = {},
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.about_content_description),
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = uiState) {
                is CountryListUiState.Loading -> CircularProgressIndicator()
                is CountryListUiState.Ready -> CountryList(false, state.countries, onCountryClick) {
                    viewModel.handleIntent(CountryListIntent.Refresh)
                }
                is CountryListUiState.Refreshing -> CountryList(true, state.countries, onCountryClick) {
                    // Do nothing - already refreshing
                }
                is CountryListUiState.Error -> RetryableError(state.message) {
                    viewModel.handleIntent(CountryListIntent.Retry)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryList(
    isRefreshing: Boolean,
    countries: List<Country>,
    onCountryClick: (Int) -> Unit,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing,
        onRefresh,
    ) {
        LazyColumn {
            itemsIndexed(countries) { index, country ->
                Country(country) {
                    onCountryClick(index)
                }
            }
        }
    }
}

@Composable
private fun Country(country: Country, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        onClick = onClick,
    ) {
        Column {
            Text(stringResource(R.string.country_name, country.name))
            Text(stringResource(R.string.country_capital, country.capital))
        }
    }
}

