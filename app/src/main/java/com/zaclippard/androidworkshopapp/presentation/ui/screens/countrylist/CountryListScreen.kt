package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.components.FavoriteStar
import com.zaclippard.androidworkshopapp.presentation.ui.components.RetryableError
import com.zaclippard.androidworkshopapp.presentation.ui.components.permissions.DeterminePermissionComponent

const val COUNTRY_LIST_TAG = "COUNTRY_LIST_TAG"
const val COUNTRY_ROW_TAG = "COUNTRY_ROW_TAG"
const val SETTINGS_ICON_TAG = "SETTINGS_ICON_TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    viewModel: CountryListViewModel = hiltViewModel(),
    onCountryClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit,
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
                    IconButton(modifier = Modifier.testTag(SETTINGS_ICON_TAG), onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings_content_description),
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .testTag(COUNTRY_LIST_TAG),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = uiState) {
                is CountryListUiState.Loading -> CircularProgressIndicator()
                is CountryListUiState.Ready -> CountryList(
                    false,
                    state.countries,
                    onCountryClick,
                    onRefresh = {
                        viewModel.handleIntent(CountryListIntent.Refresh)
                    },
                ) { country ->
                    viewModel.handleIntent(CountryListIntent.Favorite(country))
                }
                is CountryListUiState.Refreshing -> CountryList(
                    true,
                    state.countries,
                    onCountryClick,
                    onRefresh = {
                        // Do nothing - already refreshing
                    },
                ) { country ->
                    viewModel.handleIntent(CountryListIntent.Favorite(country))
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
    onFavorite: (Country) -> Unit,
) {
    PullToRefreshBox(
        isRefreshing,
        onRefresh,
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            LocationButton()

            LazyColumn {
                itemsIndexed(countries) { index, country ->
                    Country(
                        country = country,
                        onClick = {
                            onCountryClick(index)
                        },
                        onFavorite = { onFavorite(country) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Country(country: Country, onClick: () -> Unit, onFavorite: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().testTag(COUNTRY_ROW_TAG),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(8.dp).weight(1f)) {
                Text(stringResource(R.string.country_name, country.name))
                Text(stringResource(R.string.country_capital, country.capital))
            }
            FavoriteStar(country.isFavorite, onFavorite)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationButton() {
    var readyToShowLocation by remember { mutableStateOf(false) }
    var addressText: String? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    Button(
        onClick = { readyToShowLocation = true },
    ) {
        Text(stringResource(R.string.show_location_button_text))
    }

    if (readyToShowLocation) {
        DeterminePermissionComponent(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            deniedText = stringResource(R.string.location_permanently_denied_text),
            rationaleText = stringResource(R.string.location_permission_rationale),
            onPermissionGranted = {
                readyToShowLocation = false
                getLocationAddress(context) { address ->
                    addressText = address?.let { "Address:\n\n$address" } ?: "No address found."
                }
            },
            onPermissionDenied = {
                readyToShowLocation = false
            }
        )
    }

    addressText?.let {
        LocationAlertDialog(it) {
            addressText = null
        }
    }
}

private fun getLocationAddress(context: Context, onAddress: (Address?) -> Unit) {
    val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    if (permissionStatus != PackageManager.PERMISSION_GRANTED) { return }

    LocationServices
        .getFusedLocationProviderClient(context)
        .lastLocation
        .addOnSuccessListener { location ->
            if (location == null) {
                onAddress(null)
                return@addOnSuccessListener
            }

            val geocoder = Geocoder(context, Locale.current.platformLocale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val geocoderListener = @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address?>) {
                        addresses.firstOrNull()?.let { address ->
                            onAddress(address)
                        }
                    }
                }
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1,
                    geocoderListener
                )
            } else {
                val addresses =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                addresses?.firstOrNull()?.let { address ->
                    onAddress(address)
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationAlertDialog(
    addressText: String,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(addressText)
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.dismiss_button_text))
                }
            }
        }
    }
}
