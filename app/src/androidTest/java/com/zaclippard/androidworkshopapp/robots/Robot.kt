package com.zaclippard.androidworkshopapp.robots

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.test.espresso.Espresso
import com.zaclippard.androidworkshopapp.performTap
import org.junit.rules.TestRule

interface Robot

inline fun <reified R : Robot> robot(vararg args: Any, block: R.() -> Unit): R {
    val argClasses = Array<Class<*>>(args.size) { i -> args[i].javaClass }
    val constructor = R::class.java.getConstructor(*argClasses)
    return constructor.newInstance(*args).apply(block)
}

/**
 * Compose navigation
 */

inline fun <reified R : Robot> navigateToRobot(
    testRule: TestRule,
    element: SemanticsNodeInteraction,
    block: R.() -> Unit
): R {
    element.performTap()
    return robot(testRule) { block() }
}

inline fun <reified R : Robot> navigateBackToPrevRobot(
    testRule: TestRule,
    block: R.() -> Unit,
): R {
    Espresso.pressBack()
    return robot(testRule) { block() }
}
