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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.domain.Country
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

const val COUNTRY_DETAILS_CAPITAL_TAG = "COUNTRY_DETAILS_CAPITAL_TAG"
const val COUNTRY_DETAILS_POPULATION_TAG = "COUNTRY_DETAILS_POPULATION_TAG"
const val COUNTRY_DETAILS_AREA_TAG = "COUNTRY_DETAILS_AREA_TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsScreen(
    countryIndex: Int,
    viewModel: CountryDetailsViewModel = hiltViewModel(
        creationCallback = { factory: CountryDetailsViewModel.Factory ->
            factory.create(countryIndex)
        }
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
        CountryFlagAsyncImage(country.flagUrl)

        Column {
            Text(
                modifier = Modifier.testTag(COUNTRY_DETAILS_CAPITAL_TAG),
                text = stringResource(R.string.country_capital, country.capital),
            )
            Text(
                modifier = Modifier.testTag(COUNTRY_DETAILS_POPULATION_TAG),
                text = stringResource(R.string.country_population, country.population),
            )
            Text(
                modifier = Modifier.testTag(COUNTRY_DETAILS_AREA_TAG),
                text = stringResource(R.string.country_area, country.area),
            )
        }
    }
}

@Composable
private fun CountryFlagAsyncImage(url: String) {
    // Create a trust manager that does not validate certificate chains
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory = sslContext.socketFactory

    // Create OkHttpClient with the custom SSL socket factory
    val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .build()

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = {
                        okHttpClient
                    }
                )
            )
        }
        .build()

    AsyncImage(
        modifier = Modifier.height(40.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.country_flag_content_description),
        imageLoader = imageLoader,
    )
}
