package com.compose.pixivcompose.ui.module.pic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.compose.pixivcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicDetail(url: String, pressOnBack: () -> Unit = {}) {

  val painter = rememberAsyncImagePainter(model = url)
  val painterState = painter.state

  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
    FullScreenImage(
      modifier = Modifier
        .padding(0.dp, 48.dp, 0.dp, 0.dp)
        .fillMaxWidth()
        .fillMaxHeight(),
      painter = painter,
      painterState = painterState
    )
    TopAppBar(
      modifier = Modifier.fillMaxWidth().height(48.dp).align(Alignment.TopCenter),
      navigationIcon = {
        Icon(
          modifier = Modifier.width(32.dp).height(32.dp).align(Alignment.Center).clickable { pressOnBack.invoke() },
          painter = painterResource(id = R.drawable.ic_back),
          contentDescription = "",
          tint = MaterialTheme.colorScheme.tertiary
        )
      },
      title = {
        Text(
          modifier = Modifier.wrapContentWidth().wrapContentHeight().align(Alignment.Center),
          text = "PicDetail",
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    )
  }
}

@Composable
fun FullScreenImage(
  modifier: Modifier,
  painter: AsyncImagePainter,
  painterState: AsyncImagePainter.State
) {

  var scale by remember { mutableStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }

  Box(
    modifier =
      modifier.pointerInput(Unit) {
          detectTapGestures(
            onDoubleTap = {
              scale = 1f
              offset = Offset.Zero
            }
          )
        }
  ) {
    Image(
      modifier =
        Modifier
          .fillMaxSize()
          .align(Alignment.Center)
          .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
          )
          .pointerInput(Unit) {
            detectTransformGestures(
              onGesture = { centroid: Offset, pan: Offset, zoom: Float, _ ->
                offset += pan
                scale = (scale * zoom)
              }
            )
          },
      painter = painter,
      contentDescription = ""
    )

    when (painterState) {
        is AsyncImagePainter.State.Loading -> {
          CircularProgressIndicator(
            modifier = Modifier
              .wrapContentSize()
              .align(Alignment.Center),
            color = MaterialTheme.colorScheme.tertiary,
            strokeWidth = 4.dp
          )
        }
      is AsyncImagePainter.State.Success -> {

      }
      is AsyncImagePainter.State.Error -> {

      }
      else -> {

      }
    }
  }
}
