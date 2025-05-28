package com.zaclippard.androidworkshopapp.data.repositories

import app.cash.turbine.test
import com.zaclippard.androidworkshopapp.domain.sampleCountryList
import com.zaclippard.androidworkshopapp.data.database.CountryDao
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefs
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.rules.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CountryRepositoryImplTests {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Test
    fun `init starts with empty list of countries`() = runTest {
        // Arrange
        val expectedNetworkCountryList = sampleCountryList
        val mockCountryService = mockk<CountryService> {
            coEvery { getAllCountries() } returns Response.success(expectedNetworkCountryList)
        }
        val mockCountryDao = mockk<CountryDao> {
            coEvery { getAllCountries() } returns emptyList()
            coEvery { addCountries(any()) } answers {}
        }
        val mockPrefs = mockk<AndroidWorkshopPrefs> {
            every { localStorageEnabledStream } returns MutableStateFlow(true)
            every { rotationEnabledStream } returns MutableStateFlow(true)
        }

        // Act
        val sut = CountryRepositoryImpl(mockCountryService, mockCountryDao, mockPrefs)

        // Assert
        sut.countryListResultStream.test {
            assertEquals(Result.success<List<Country>>(emptyList()), expectMostRecentItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `fetchCountries updates list of countries`() = runTest {
        // Arrange
        val expectedNetworkCountryList = sampleCountryList
        var expectedDatabaseCountryList = emptyList<Country>()
        val mockCountryService = mockk<CountryService> {
            coEvery { getAllCountries() } returns Response.success(expectedNetworkCountryList)
        }
        val countriesCaptured = mutableListOf<Country>()
        val mockCountryDao = mockk<CountryDao> {
            coEvery { getAllCountries() } answers { expectedDatabaseCountryList }
            coEvery { addCountries(*varargAll { countriesCaptured.add(it) }) } answers {
                expectedDatabaseCountryList = countriesCaptured
            }
        }
        val mockPrefs = mockk<AndroidWorkshopPrefs> {
            every { localStorageEnabledStream } returns MutableStateFlow(true)
            every { rotationEnabledStream } returns MutableStateFlow(true)
        }
        val sut = CountryRepositoryImpl(mockCountryService, mockCountryDao, mockPrefs)

        // Act
        sut.fetchCountries(false)

        // Assert
        sut.countryListResultStream.test {
            assertEquals(Result.success(expectedNetworkCountryList), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `fetchCountries updates list of countries with no local storage`() = runTest {
        // Arrange
        val expectedNetworkCountryList = sampleCountryList
        val mockCountryService = mockk<CountryService> {
            coEvery { getAllCountries() } returns Response.success(expectedNetworkCountryList)
        }
        val mockCountryDao = mockk<CountryDao>()
        val mockPrefs = mockk<AndroidWorkshopPrefs> {
            every { localStorageEnabledStream } returns MutableStateFlow(false)
            every { rotationEnabledStream } returns MutableStateFlow(true)
        }
        val sut = CountryRepositoryImpl(mockCountryService, mockCountryDao, mockPrefs)

        // Act
        sut.fetchCountries(false)

        // Assert
        sut.countryListResultStream.test {
            assertEquals(Result.success(expectedNetworkCountryList), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getCountry returns country`() = runTest {
        // Arrange
        val expectedCountryList = sampleCountryList
        val mockCountryService = mockk<CountryService> {
            coEvery { getAllCountries() } returns Response.success(expectedCountryList)
        }
        val mockCountryDao = mockk<CountryDao> {
            coEvery { getAllCountries() } returns expectedCountryList
        }
        val mockPrefs = mockk<AndroidWorkshopPrefs> {
            every { localStorageEnabledStream } returns MutableStateFlow(false)
            every { rotationEnabledStream } returns MutableStateFlow(true)
        }
        val sut = CountryRepositoryImpl(mockCountryService, mockCountryDao, mockPrefs)
        sut.fetchCountries(false)

        // Act
        val country = sut.getCountry(1)

        // Assert
        assertEquals(sampleCountryList[1], country)
    }

    @Test
    fun `getCountry returns null when no countries cached`() = runTest {
        // Arrange
        val expectedCountryList = sampleCountryList
        val mockCountryService = mockk<CountryService> {
            coEvery { getAllCountries() } returns Response.success(expectedCountryList)
        }
        val mockCountryDao = mockk<CountryDao> {
            coEvery { getAllCountries() } returns expectedCountryList
        }
        val mockPrefs = mockk<AndroidWorkshopPrefs> {
            every { localStorageEnabledStream } returns MutableStateFlow(false)
            every { rotationEnabledStream } returns MutableStateFlow(true)
        }
        val sut = CountryRepositoryImpl(mockCountryService, mockCountryDao, mockPrefs)

        // Act
        val country = sut.getCountry(1)

        // Assert
        assertNull(country)
    }
}
