package com.compose.pixivcompose.ui.module.img

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Precision
import com.compose.pixivcompose.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImgDetail(url: String, pressOnBack: () -> Unit = {}) {

  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  val painter = rememberAsyncImagePainter(model = url)
  val painterState = painter.state

  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
    FullScreenImage(
      modifier = Modifier.padding(0.dp, 48.dp, 0.dp, 0.dp).fillMaxWidth().fillMaxHeight(),
      painter = painter,
      painterState = painterState
    )
    TopAppBar(
      modifier = Modifier.fillMaxWidth().height(48.dp).align(Alignment.TopCenter),
      navigationIcon = {
        IconButton(
          onClick = pressOnBack,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
          )
        }
      },
      title = {
        Text(
          modifier = Modifier.wrapContentWidth().wrapContentHeight().align(Alignment.Center),
          text = "ImgDetail",
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      },
      actions = {
        IconButton(
          onClick = {
            scope.launch {
              val drawable =
                painter.imageLoader
                  .execute(
                    ImageRequest.Builder(context).data(url).size(Int.MAX_VALUE).precision(Precision.INEXACT).build()
                  )
                  .drawable
              val bitmap = (drawable as BitmapDrawable).bitmap
              if (bitmap != null) {
                saveBitmapToStorage(context, bitmap)
              }
            }
          },
        ) {
          Icon(
            modifier = Modifier.padding(6.dp),
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
          )
        }
      }
    )
  }
}

@Composable
fun FullScreenImage(
  modifier: Modifier,
  painter: AsyncImagePainter,
  painterState: AsyncImagePainter.State,
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
        Modifier.fillMaxSize()
          .align(Alignment.Center)
          .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y)
          .pointerInput(Unit) {
            detectTransformGestures(
              onGesture = { _: Offset, pan: Offset, zoom: Float, _ ->
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
          modifier = Modifier.wrapContentSize().align(Alignment.Center),
          color = MaterialTheme.colorScheme.tertiary,
          strokeWidth = 4.dp
        )
      }
      is AsyncImagePainter.State.Success -> {}
      is AsyncImagePainter.State.Error -> {}
      else -> {}
    }
  }
}

private suspend fun saveBitmapToStorage(context: Context, bitmap: Bitmap) {
  withContext(Dispatchers.IO) {
    val filename = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      context.contentResolver?.also { resolver ->
        val contentValues =
          ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
          }
        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
      }
    } else {
      val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
      val image = File(imagesDir, filename)
      fos = FileOutputStream(image)
    }
    fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
  }
  Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
}
