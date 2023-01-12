package com.compose.pixivcompose.ui.module.pic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChangeIgnoreConsumed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.compose.pixivcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicDetail(url: String, viewModel: PicDetailViewModel, pressOnBack: () -> Unit = {}) {

  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
    TopAppBar(
      modifier = Modifier.fillMaxWidth().height(48.dp).align(Alignment.TopCenter),
      navigationIcon = {
        Icon(
          modifier = Modifier.width(32.dp).height(32.dp).align(Alignment.Center),
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

    FullScreenImage(modifier = Modifier.padding(0.dp, 48.dp, 0.dp, 0.dp).fillMaxSize(), url = url)
  }
}

@Composable
fun FullScreenImage(
  modifier: Modifier,
  url: String,
) {

  var scale by remember { mutableStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }
  val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
    scale = (zoomChange * scale).coerceAtLeast(1f)
  }

  Surface(
    modifier =
      modifier.fillMaxSize().pointerInput(Unit) {
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
        Modifier.fillMaxSize()
          .transformable(state = state)
          .graphicsLayer {
            scaleX = scale
            scaleY = scale
            translationX = offset.x
            translationY = offset.y
          }
          .pointerInput(Unit) {
            awaitPointerEventScope {
              val event = awaitPointerEvent()
              if (event.changes.size == 1) {
                event.changes[0].consume()
              } else {
                event.changes.forEach { it.positionChangeIgnoreConsumed() }
              }
            }
            detectDragGestures { change, dragAmount -> offset += dragAmount }
          },
      painter = rememberAsyncImagePainter(model = url),
      contentDescription = ""
    )
  }
}
