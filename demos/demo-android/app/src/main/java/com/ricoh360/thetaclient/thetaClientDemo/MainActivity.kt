package com.ricoh360.thetaclient.thetaClientDemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "mainScreen") {
                composable("mainScreen") {
                    MainScreen(
                        toInfo = { navController.navigate("infoScreen") },
                        toPhotoList = { navController.navigate("photoListScreen") },
                        toPreview = { navController.navigate("previewScreen") }
                    )
                }
                composable("previewScreen") {
                    PreviewScreen(
                        toPhoto = { photoUrl ->
                            navController.navigate("photoScreen/${URLEncoder.encode(photoUrl, "UTF-8")}")
                        }
                    )
                }
                composable("infoScreen") {
                    PhotoListScreen(
                        toPhoto = { fileName, photoUrl ->
                            navController.navigate("photoScreen/$fileName/${URLEncoder.encode(photoUrl, "UTF-8")}")
                        }
                    )
                }
                composable("photoListScreen") {
                    PhotoListScreen(
                        toPhoto = { fileName, photoUrl ->
                            navController.navigate("photoScreen/$fileName/${URLEncoder.encode(photoUrl, "UTF-8")}")
                        }
                    )
                }
                composable("photoScreen/{photoUrl}") { backStackEntry ->
                    PhotoScreen(
                        photoUrl = URLDecoder.decode(backStackEntry.arguments?.getString("photoUrl"), "UTF-8")
                    )
                }
                composable("photoScreen/{fileName}/{photoUrl}") { backStackEntry ->
                    PhotoScreen(
                        fileName = backStackEntry.arguments?.getString("fileName"),
                        photoUrl = URLDecoder.decode(backStackEntry.arguments?.getString("photoUrl"), "UTF-8")
                    )
                }
            }
        }
    }
}
