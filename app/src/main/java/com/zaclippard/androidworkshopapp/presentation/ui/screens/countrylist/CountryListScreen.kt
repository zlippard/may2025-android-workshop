package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.components.FavoriteStar
import com.zaclippard.androidworkshopapp.presentation.ui.components.RetryableError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(
    viewModel: CountryListViewModel = viewModel(factory = CountryListViewModel.Factory),
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
                    IconButton(onClick = onSettingsClick) {
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
                .fillMaxSize(),
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
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
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
    var lastAddressFound by remember { mutableStateOf<Address?>(null) }

    Button(
        onClick = { readyToShowLocation = true },
    ) {
        Text(stringResource(R.string.show_location_button_text))
    }

    if (readyToShowLocation) {
        DetermineLocationComponent { address ->
            lastAddressFound = address
            readyToShowLocation = false
        }
    }

    lastAddressFound?.let { address ->
        BasicAlertDialog(
            onDismissRequest = {
                lastAddressFound = null
            }
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
                    Text("Address found:\n\n$address")
                    Button(onClick = {
                        lastAddressFound = null
                    }) {
                        Text(stringResource(R.string.dismiss_button_text))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetermineLocationComponent(
    onLastAddressFound: (Address?) -> Unit,
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    var locationPermissionState by rememberSaveable {
        mutableStateOf(PermissionState.ASK)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        locationPermissionState = if (isGranted) {
            PermissionState.GRANTED
        } else {
            PermissionState.PERMANENTLY_DENIED
        }
    }

    when (locationPermissionState) {
        PermissionState.ASK -> {
            LaunchedEffect(Unit) {
                val locationPermissionStatus = ContextCompat.checkSelfPermission(
                    context,
                    locationPermission,
                )
                locationPermissionState =
                    if (locationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
                        PermissionState.GRANTED
                    } else {
                        PermissionState.DENIED
                    }
            }
        }

        PermissionState.GRANTED -> {
            LocationServices
                .getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        onLastAddressFound(null)
                        return@addOnSuccessListener
                    }

                    val geocoder = Geocoder(context, Locale.current.platformLocale)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val geocoderListener = @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: List<Address?>) {
                                addresses.firstOrNull()?.let { address ->
                                    onLastAddressFound(address)
                                }
                            }
                        }
                        geocoder.getFromLocation(location.latitude, location.longitude, 1, geocoderListener)
                    } else {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        addresses?.firstOrNull()?.let { address ->
                            onLastAddressFound(address)
                        }
                    }
                }
        }

        PermissionState.DENIED -> {
            val shouldShowRationale =
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    locationPermission,
                )
            if (shouldShowRationale) {
                LocationPermissionRationale { launcher.launch(locationPermission) }
            } else {
                launcher.launch(locationPermission)
            }
        }

        PermissionState.PERMANENTLY_DENIED -> {
            LocationPermanentlyDeniedComponent {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
private fun LocationPermanentlyDeniedComponent(onGoToAppSettings: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.location_permanently_denied_text))
        Button(onClick = onGoToAppSettings) {
            Text(stringResource(R.string.go_to_app_settings_button_text))
        }
    }
}

@Composable
private fun LocationPermissionRationale(onAcceptLocationPermissionClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.location_permission_rationale))
        Button(onClick = onAcceptLocationPermissionClick) {
            Text(stringResource(R.string.accept_location_permission_button_text))
        }
    }
}

private enum class PermissionState {
    ASK,
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED,
}
