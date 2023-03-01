package com.ricoh360.thetaclient.thetaClientDemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebResourceErrorCompat
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Viewer for equirectangular image
 *
 * @param photoUrl URL of an image.
 */
@Composable
fun Viewer360(photoUrl: String) {
    var photoDataUrl: String? by remember { mutableStateOf(null) }

    LaunchedEffect(photoUrl) {
        photoDataUrl = getPhotoDataUrl(photoUrl)
    }

    photoDataUrl?.let {
        Viewer360Core(it)
    }
}

/**
 * Viewer for preview image.
 *
 * @param bitmap a frame of preview
 */
@Composable
fun Viewer360(bitmap: Bitmap) {
    var previewDataUrl: String? by remember { mutableStateOf(null) }

    LaunchedEffect(bitmap) {
        previewDataUrl = getPreviewDataUrl(bitmap)
    }

    previewDataUrl?.let {
        Viewer360Core(it)
    }
}

/**
 * 360 photo viewer.
 *
 * @param photoDataUrl Data URL of equirectangular Jpeg image.
 */
@Composable
fun Viewer360Core(photoDataUrl: String) {
    /**
     * Injecting object to WebView's JavaScript.
     */
    class JsObject() {
        @JavascriptInterface
        fun getPhotoUrl(): String {
            Timber.d("called getPhotoUrl(): ${photoDataUrl.substring(0, 40)}...")
            return photoDataUrl
        }
    }

    /**
     * A webView client to load local files from "assets" directory.
     *
     * @property assetLoader
     */
    class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            super.shouldOverrideUrlLoading(view, request)
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Timber.d("page finished: ${url}")
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceErrorCompat
        ) {
            super.onReceivedError(view, request, error)
            Timber.d("Request: ${request.method} ${request.url}")
            Timber.d("Received an error: ${error.errorCode}: ${error.description}")
        }
    }

    val PATH_ASSETS = "/assets/"
    val HTML_FILE_NAME = "index.html"
    val URL_PREFIX_FOR_LOCAL_RESOURCES = "http://appassets.androidplatform.net" // defined in WebViewAssetLoader
    lateinit var assetLoader: WebViewAssetLoader
    AndroidView(
        factory = { context ->
            assetLoader = WebViewAssetLoader.Builder()
                .setHttpAllowed(true)
                .addPathHandler(PATH_ASSETS, WebViewAssetLoader.AssetsPathHandler(context))
                .build()
            WebView(context).apply {
                settings.allowFileAccess = true
                settings.javaScriptEnabled = true
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE)
                addJavascriptInterface(JsObject(), "injectedObject")
                webViewClient = LocalContentWebViewClient(assetLoader)
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                /* webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(cm: ConsoleMessage?): Boolean {
                        cm?.let {
                            Timber.d("[CONSOLE] ${cm.message()} , source: ${cm.sourceId()} (${cm.lineNumber()})")
                        }
                        return super.onConsoleMessage(cm)
                    }
                }*/
                val headers = mapOf("Access-Control-Allow-Origin" to "*") // Need to get an image in Theta.
                loadUrl(
                    URL_PREFIX_FOR_LOCAL_RESOURCES + PATH_ASSETS + HTML_FILE_NAME,
                    headers
                )
            }
        },
        update = { webView ->
            webView.evaluateJavascript("viewer.viewers[0].switchScene('sceneEqui');console.log('js evaled');", null)
            //webView.evaluateJavascript("viewer.removeViewer(); viewer.addNewViewer();console.log('evaled');", null)
        },
        modifier = Modifier.fillMaxSize()
    )

}

/**
 * Download Jpeg and encode to data URL.
 *
 * @param photoUrl URL of Jpeg image
 * @return Generated photo data URL
 */
suspend fun getPhotoDataUrl(photoUrl: String): String? = withContext(Dispatchers.IO) {
    val DATA_URL_PREFIX = "data:image/jpeg;base64,"
    var dataUrl: String? = null
    HttpClient(CIO).use { httpClient ->
        val response = httpClient.get(photoUrl)
        if(response.status.isSuccess()) {
            val byteArray: ByteArray = response.body()
            dataUrl = DATA_URL_PREFIX + Base64.getEncoder().encodeToString(downSizeImage(byteArray))
        } else {
            Timber.e("Failed to download a photo")
        }
    }
    dataUrl
}

/**
 * Encode preview frame to data URL.
 *
 * @param bitmap a frame of preview
 * @return generated data URL
 */
suspend fun getPreviewDataUrl(bitmap: Bitmap): String = withContext(Dispatchers.Default) {
    val DATA_URL_PREFIX = "data:image/jpeg;base64,"
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    bitmap.recycle()
    DATA_URL_PREFIX + Base64.getEncoder().encodeToString(baos.toByteArray())
}

/**
 * Downsize an image to be able to display in a Viewer360Core.
 *
 * @param image Equirectangular image
 * @return Downsized image
 */
fun downSizeImage(image: ByteArray): ByteArray {
    val MAX_WIDTH = 2048 // the value must be equal to or less than 4096.
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeByteArray(image, 0, image.size, options)
    Timber.d("Jpeg width; ${options.outWidth}")
    if(options.outWidth > MAX_WIDTH) {
        val scaleFactor = MAX_WIDTH / options.outWidth.toFloat()
        val scale = Matrix()
        scale.postScale(scaleFactor, scaleFactor)
        val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, scale, false)
        Timber.d("Resized width: ${resizedBitmap.width}")
        bitmap.recycle()
        ByteArrayOutputStream().use {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            resizedBitmap.recycle()
            return it.toByteArray()
        }
    }
    return image
}