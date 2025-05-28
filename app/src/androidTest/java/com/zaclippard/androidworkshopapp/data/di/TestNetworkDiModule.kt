package com.zaclippard.androidworkshopapp.data.di

import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.domain.sampleCountryList
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Response
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkDiModule::class]
)
class TestNetworkDiModule {
    @Provides
    @Singleton
    fun provideCountryService(): CountryService = object : CountryService {
        override suspend fun getAllCountries(): Response<List<Country>> =
            Response.success(sampleCountryList)
    }
}

