package com.ricoh360.thetaclient.thetaClientDemo

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import com.ricoh360.thetaclient.thetaClientDemo.ui.theme.ThetaSimpleAndroidAppTheme
import timber.log.Timber

/**
 * A screen to display a sphere photo.
 * @param fileName Downloaded photo is saved into.
 * @param photoUrl URL to get a sphere photo.
 */
@Composable
fun PhotoScreen(fileName: String?, photoUrl: String?) {
    Timber.i("photoUrl: ${photoUrl ?: "null"}")

    ThetaSimpleAndroidAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(fileName?: "") })
            }
        ) {
            photoUrl?.let {
                Viewer360(photoUrl)
            }
        }
    }
}


/**
 * A screen to display a sphere photo.
 * @param photoUrl URL to get a sphere photo.
 */
@Composable
fun PhotoScreen(photoUrl: String) {
    Timber.i("PhotoScreen() $photoUrl")
    val match = Regex("/([^/]+)$").find(photoUrl) // Get last segment of the URL
    val fileName = match?.groups?.get(1)?.value
    PhotoScreen(fileName, photoUrl)
}