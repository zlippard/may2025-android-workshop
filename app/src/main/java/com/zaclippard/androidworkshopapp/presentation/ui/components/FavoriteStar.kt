package com.zaclippard.androidworkshopapp.presentation.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme
import com.zaclippard.androidworkshopapp.presentation.ui.theme.Pink80
import com.zaclippard.androidworkshopapp.presentation.ui.theme.Purple80

@Composable
fun FavoriteStar(isFavorite: Boolean, onClick: () -> Unit) {
    val favoriteTransition = updateTransition(
        targetState = isFavorite,
    )
    val scaleAnimation by favoriteTransition.animateFloat(
        transitionSpec = {
            if (!isFavorite) tween(0) else {
                keyframes {
                    durationMillis = 1_000
                    1.0f at 0 using FastOutSlowInEasing
                    0.75f at 400 using FastOutSlowInEasing
                    1.5f at 700 using FastOutSlowInEasing
                    1.0f at 1_000 using FastOutSlowInEasing
                }
            }
        },
    ) { state ->
        if (state) {
            1.0f
        } else {
            1.0f
        }
    }
    val colorAnimation by favoriteTransition.animateColor(
        transitionSpec = {
            if (!isFavorite) tween(0) else {
                keyframes {
                    durationMillis = 1_000
                    Purple80 at 700 using FastOutSlowInEasing
                    Pink80 at 1_000 using FastOutSlowInEasing
                }
            }
        },
    ) { state ->
        if (state) {
            Pink80
        } else {
            LocalContentColor.current
        }
    }
    val rotationAnimation by favoriteTransition.animateFloat(
        transitionSpec = { tween(if (!isFavorite) 0 else 750) },
    ) { state ->
        if (state) {
            360.0f
        } else {
            0.0f
        }
    }
    IconButton(
        onClick = {
            onClick()
        },
    ) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) {
                    R.drawable.star_filled
                } else {
                    R.drawable.star_outline
                },
            ),
            contentDescription = stringResource(R.string.favorite),
            modifier = Modifier
                .padding(all = 8.dp)
                .size(32.dp)
                .graphicsLayer(
                    scaleX = scaleAnimation,
                    scaleY = scaleAnimation,
                    rotationZ = rotationAnimation,
                ),
            tint = colorAnimation,
        )
    }
}

@Preview
@Composable
fun FavoriteStarPreview() {
    AndroidWorkshopAppTheme {
        FavoriteStar(isFavorite = true) {}
    }
}
