package com.ricoh360.thetaclient.thetaClientDemo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.thetaClientDemo.ui.theme.ThetaSimpleAndroidAppTheme
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Display live preview.
 * @param toPhoto NavController.navigate() to PhotoScreen.
 * @param viewModel holds connecting theta information and get live preview.
 */
@Composable
fun PreviewScreen(toPhoto: (photoUrl: String) -> Unit, viewModel: ThetaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    class TakenCallback : PhotoCapture.TakePictureCallback {
        override fun onSuccess(fileUrl: String) {
            Timber.i("takePicture onSuccess: $fileUrl")
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                toPhoto(fileUrl) // need to execute on the main thread.
            }
        }

        override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
            Timber.e(exception)
        }
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        onDispose {
            viewModel.stopPreview()
            Timber.i("Preview stopped")
        }
    }

    ThetaSimpleAndroidAppTheme {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.takePhoto(TakenCallback())
                }) {}
            },
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                viewModel.startPreview()
                //viewModel.startPreviewWithCallback()
                val bitmap by viewModel.previewFlow.collectAsState()
                bitmap?.let {
                    // Viewer360(bitmap!!)
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}




