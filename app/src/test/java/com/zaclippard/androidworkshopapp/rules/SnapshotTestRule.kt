package com.zaclippard.androidworkshopapp.rules

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6_PRO
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

class SnapshotTestRule : TestWatcher() {

    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_6_PRO,
        theme = "android:Theme.Material.Light.NoActionBar",
        maxPercentDifference = 0.1,
    )

    override fun apply(base: Statement, description: Description): Statement {
        return paparazzi.apply(
            base,
            Description.createSuiteDescription(
                description.displayName.replace(" ", "_"),
                description.annotations.toTypedArray(),
            )
        )
    }

    fun snapshot(composable: @Composable () -> Unit) {
        configurations.forEach { name, deviceConfig ->
            paparazzi.unsafeUpdateConfig(deviceConfig)
            paparazzi.snapshot(name, composable)
        }
    }

    private val configurations = mapOf(
        "day" to PIXEL_6_PRO.copy(nightMode = NightMode.NOTNIGHT),
        "night" to PIXEL_6_PRO.copy(nightMode = NightMode.NIGHT),
    )
}
