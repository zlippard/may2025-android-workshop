package com.zaclippard.androidworkshopapp.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaclippard.androidworkshopapp.R
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme
import com.zaclippard.androidworkshopapp.presentation.ui.theme.Pink80

@Composable
fun FavoriteStar(isFavorite: Boolean, onClick: () -> Unit) {
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
                .size(32.dp),
            tint = if (isFavorite) {
                Pink80
            } else {
                Color.Black
            }
        )
    }
}

@Preview
@Composable
fun FavoriteStarPreview() {
    AndroidWorkshopAppTheme {
        FavoriteStar(isFavorite = false) {}
    }
}

@Preview
@Composable
fun AlreadyFavoritedStarPreview() {
    AndroidWorkshopAppTheme {
        FavoriteStar(isFavorite = true) {}
    }
}

