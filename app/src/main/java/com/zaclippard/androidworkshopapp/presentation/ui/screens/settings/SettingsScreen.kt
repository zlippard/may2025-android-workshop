package com.zaclippard.androidworkshopapp.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zaclippard.androidworkshopapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
    onNavigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back_content_description),
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SettingsToggleRow(
                label = stringResource(id = R.string.settings_enable_local_storage),
                isToggleChecked = uiState.localStorageEnabled,
                onToggleChanged = { viewModel.handleIntent(SettingsIntent.TOGGLE_LOCAL_STORAGE) },
            )
            SettingsToggleRow(
                label = stringResource(id = R.string.settings_enable_rotation),
                isToggleChecked = uiState.rotationEnabled,
                onToggleChanged = { viewModel.handleIntent(SettingsIntent.TOGGLE_ROTATION) },
            )
        }
    }
}

@Composable
fun SettingsToggleRow(
    label: String,
    isToggleChecked: Boolean,
    onToggleChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        Switch(
            checked = isToggleChecked,
            onCheckedChange = onToggleChanged,
        )
    }
}

@Preview(widthDp = 320)
@Composable
fun SettingsToggleRowPreview() {
    SettingsToggleRow(
        label = "Dark Mode",
        isToggleChecked = true,
        onToggleChanged = {},
    )
}
