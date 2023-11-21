package com.ricoh360.thetaclient.thetaClientDemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.thetaClientDemo.ui.theme.ThetaSimpleAndroidAppTheme

/**
 * Display thumbnail list.
 * @param toPhoto NavController.navigate() to PhotoScreen.
 * @param viewModel holds connecting theta information and photo file list.
 */
@Composable
fun PhotoListScreen(
    toPhoto: (fileName: String, photoUrl: String) -> Unit,
    viewModel: ThetaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val oscInfo: ThetaRepository.ThetaInfo? by viewModel.thetaInfoState.collectAsState(initial = null)
    val thetaFiles: ThetaRepository.ThetaFiles? by viewModel.thetaFilesState.collectAsState(initial = null)

    ThetaSimpleAndroidAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(oscInfo?.model + ": " + oscInfo?.serialNumber)
                })
            }
        ) {
            thetaFiles?.also {
                val thumbWidth = 100.dp
                val pad = 24.dp
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(pad)
                ) {
                    items(it.fileList) { file ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            ThumbnailImage(
                                url = file.thumbnailUrl,
                                width = thumbWidth,
                                onClick = { toPhoto(file.name, file.fileUrl) },
                            )
                            Text(
                                text = file.name,
                                modifier = Modifier.padding(pad)
                            )
                        }
                    }
                }
            } ?: Column(modifier = Modifier.padding(24.dp)) {
                Text("Connect your Theta with WiFi using Android setting application.")
            }
        }
    }
}


/**
 * Display a thumbnail.
 * @param url of a thumbnail.
 * @param width of a thumbnail.
 * @param onClick An action when the thumbnail is clicked.
 */
@Composable
fun ThumbnailImage(url: String, width: Dp, onClick: () -> Unit) {
    AsyncImage(
        model = url,
        contentDescription = "A thumbnail",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(width)
            .clickable { onClick() },
    )
}