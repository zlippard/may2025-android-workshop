package com.zaclippard.androidworkshopapp.data.di

import android.content.Context
import com.zaclippard.androidworkshopapp.data.database.CountryDatabase
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefs
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefsImpl
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {
    @Provides
    @Singleton
    fun providesCountryDatabase(@ApplicationContext applicationContext: Context): CountryDatabase =
        CountryDatabase.buildDatabase(applicationContext)

    @Provides
    @Singleton
    fun providesPrefs(@ApplicationContext applicationContext: Context): AndroidWorkshopPrefs =
        AndroidWorkshopPrefsImpl(applicationContext)

    @Provides
    @Singleton
    fun providesCountryRepository(
        service: CountryService,
        database: CountryDatabase,
        prefs: AndroidWorkshopPrefs,
    ): CountryRepository = CountryRepositoryImpl(service, database.countryDao(), prefs)
}
