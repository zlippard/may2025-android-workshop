package com.zaclippard.androidworkshopapp.presentation.ui.components.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaclippard.androidworkshopapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionRationaleDialog(
    rationaleText: String,
    onAcceptPermissionClick: () -> Unit,
    onCancel: () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Text(rationaleText)
                Row {
                    Button(onClick = onAcceptPermissionClick) {
                        Text(stringResource(R.string.accept_permission_button_text))
                    }
                    Button(onClick = onCancel) {
                        Text(stringResource(R.string.cancel_button_text))
                    }
                }
            }
        }
    }
}
