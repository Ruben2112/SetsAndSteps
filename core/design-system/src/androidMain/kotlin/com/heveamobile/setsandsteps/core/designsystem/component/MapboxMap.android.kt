package com.heveamobile.setsandsteps.core.designsystem.component

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapOptions
import com.mapbox.maps.MapboxDelicateApi
import com.mapbox.maps.applyDefaultParams
import com.mapbox.maps.extension.compose.ComposeMapInitOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.toCameraOptions

@OptIn(MapboxDelicateApi::class)
@SuppressLint("ClickableViewAccessibility")
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
actual fun MapboxMap(
    boundingBox: List<Double>,
    modifier: Modifier,
) {
    val mapViewportState = rememberMapViewportState()

    /** A mapbox_access_token string resource containing a valid Mapbox access token is required in
     * androidApp/src/main/res/values/mapbox_access_token.xml.
     **/
    MapboxMap(
        mapViewportState = mapViewportState,
        composeMapInitOptions = ComposeMapInitOptions(
            MapOptions
                .Builder()
                .applyDefaultParams(LocalContext.current)
                .build(),
            textureView = true,
        ),
        scaleBar = {},
    ) {
        MapEffect(boundingBox) { mapView ->
            // "Override" the touch event to prevent parent views from intercepting the touch events
            mapView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> v.parent.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
                false
            }

            val mapboxMap = mapView.mapboxMap

            // Update the camera to the bounding box
            val updateCamera = {
                if (boundingBox.size >= 4) {
                    val cameraOptions = mapboxMap.cameraForCoordinates(
                        coordinates = listOf(
                            Point.fromLngLat(
                                boundingBox[0],
                                boundingBox[1],
                            ),
                            Point.fromLngLat(
                                boundingBox[2],
                                boundingBox[3],
                            ),
                        ),
                        camera = mapboxMap.cameraState.toCameraOptions(),
                        coordinatesPadding = EdgeInsets(
                            50.0,
                            50.0,
                            50.0,
                            50.0,
                        ),
                        null,
                        null,
                    )
                    mapboxMap.setCamera(cameraOptions)
                }
            }

            // We should wait with updating the camera until the style is loaded, else the camera
            // will not be updated correctly
            val style = mapboxMap.style
            if (style != null && style.isStyleLoaded()) {
                // Style is already here, update immediately
                updateCamera()
            } else {
                // Wait for the style loaded event
                mapboxMap.subscribeStyleLoaded {
                    updateCamera()
                }
            }
        }
    }
}