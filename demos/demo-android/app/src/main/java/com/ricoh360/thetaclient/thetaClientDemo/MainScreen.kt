package com.ricoh360.thetaclient.thetaClientDemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ricoh360.thetaclient.thetaClientDemo.ui.theme.ThetaSimpleAndroidAppTheme

/**
 * Main menu screen.
 * @param toPhotoList NavController.navigate() to PhotoLstScreen.
 * @param toPreview NavController.navigate() to PreviewScreen.
 */
@Composable
fun MainScreen(toPhotoList: () -> Unit, toPreview: () -> Unit) {
    ThetaSimpleAndroidAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(stringResource(id = R.string.app_name))
                })
            }
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { toPreview() }) {
                    Text("Take a photo")
                }
                Button(onClick = { toPhotoList() }) {
                    Text("List photos")
                }
            }
        }
    }
}
