package io.github.grishaninvyacheslav.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.grishaninvyacheslav.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Container()
            }
        }
    }
}

@Composable
fun Container() {
    Box(modifier = Modifier.fillMaxSize()) {
        ZoomableImage()
    }
}

// https://stackoverflow.com/a/66027497/11883985
@Composable
fun ZoomableImage() {
    val scale = remember { mutableStateOf(1f) }
    //val rotationState = remember { mutableStateOf(1f) }
    val offsetState = remember { mutableStateOf(Offset(0f, 0f)) }
    Box(
        modifier = Modifier
            .clip(RectangleShape) // Clip the box content
            .fillMaxSize() // Give the size you want...
            .background(Color.White)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    Log.d(
                        "[MYLOG]",
                        "centroid($centroid) pan($pan) zoom($zoom) rotation($rotation)"
                    )
                    scale.value *= zoom
                    if (zoom == 1f) {
                        offsetState.value += pan
                    } else {
                        offsetState.value *= zoom
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offsetState.value.x,
                    translationY = offsetState.value.y,
                )
                .background(Color.White),
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center),
                //.fillMaxSize(),
                contentDescription = "Map",
                //contentScale = ContentScale.Fit,
                painter = painterResource(R.drawable.map)
            )
            Image(
                painter = painterResource(R.drawable.point),
                contentDescription = "point",
                Modifier
                    .offset(21.dp, 82.dp)
                    .graphicsLayer(
                        scaleX = 1 / scale.value,
                        scaleY = 1 / scale.value,
                    )
                    .offset(0.dp, (-37 / 2).dp),
                colorFilter = ColorFilter.tint(Color.Blue)
            )
        }
        Image(
            painter = painterResource(R.drawable.point),
            contentDescription = "point",
            Modifier
                .align(
                    Alignment.Center
                )
                .offset(0.dp, (-37 / 2).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(
    font: FontFamily = FontFamily(
        Font(R.font.neonderthaw_regular)
    )
) {
    MyApplicationTheme {
        Container()
    }
}