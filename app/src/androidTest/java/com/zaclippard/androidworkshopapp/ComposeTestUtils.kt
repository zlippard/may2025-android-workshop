package com.zaclippard.androidworkshopapp

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo

fun SemanticsNodeInteraction.performTap(scrollTo: Boolean = false): SemanticsNodeInteraction {
    if (scrollTo) { performScrollTo() }
    return performClick()
}
