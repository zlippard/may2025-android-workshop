package com.zaclippard.androidworkshopapp.presentation.ui.components.permissions

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
internal fun DeterminePermissionComponent(
    permission: String,
    deniedText: String,
    rationaleText: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    var permissionState by rememberSaveable {
        mutableStateOf(PermissionState.ASK)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionState = if (isGranted) {
            PermissionState.GRANTED
        } else {
            PermissionState.PERMANENTLY_DENIED
        }
    }

    when (permissionState) {
        PermissionState.ASK -> {
            LaunchedEffect(Unit) {
                val permissionStatus = ContextCompat.checkSelfPermission(
                    context,
                    permission,
                )
                permissionState =
                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        PermissionState.GRANTED
                    } else {
                        PermissionState.DENIED
                    }
            }
        }

        PermissionState.GRANTED -> {
            onPermissionGranted()
        }

        PermissionState.DENIED -> {
            val shouldShowRationale =
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission,
                )
            if (shouldShowRationale) {
                PermissionRationale(
                    rationaleText,
                    onAcceptPermissionClick = { launcher.launch(permission) },
                    onCancel = onPermissionDenied,
                )
            } else {
                launcher.launch(permission)
            }
        }

        PermissionState.PERMANENTLY_DENIED -> {
            PermanentlyDeniedComponent(
                deniedText,
                onGoToAppSettings = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                    onPermissionDenied()
                },
                onCancel = onPermissionDenied,
            )
        }
    }
}
