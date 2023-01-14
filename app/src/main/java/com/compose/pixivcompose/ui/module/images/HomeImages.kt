package com.compose.pixivcompose.ui.module.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.compose.pixivcompose.R
import com.compose.pixivcompose.network.response.ResponseImgBean

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeImages(
  modifier: Modifier = Modifier,
  images: List<ResponseImgBean>,
  onClickImg: (String) -> Unit,
  onClickRefresh: () -> Unit
) {
  Box(modifier = modifier) {
    LazyVerticalStaggeredGrid(
      modifier = Modifier.fillMaxWidth().fillMaxHeight(),
      columns = StaggeredGridCells.Fixed(2),
      contentPadding = PaddingValues(8.dp, 2.dp, 8.dp, 0.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      items(images) { ImgItem(item = it, onClickImg = onClickImg) }
    }

    FloatingActionButton(
      modifier = Modifier.padding(24.dp).align(Alignment.BottomEnd),
      containerColor = MaterialTheme.colorScheme.tertiary,
      onClick = onClickRefresh
    ) {
      Image(
        modifier = Modifier.padding(8.dp).width(48.dp).height(48.dp),
        painter = painterResource(R.drawable.ic_refresh),
        contentDescription = null
      )
    }
  }
}

@Composable
fun ImgItem(modifier: Modifier = Modifier, item: ResponseImgBean, onClickImg: (String) -> Unit) {
  val configuration = LocalConfiguration.current
  val imgWidth = item.width.toFloat()
  val imgHeight = item.height.toFloat()
  val itemWidthDp = (configuration.screenWidthDp / 2) - 8
  val itemWidthPx = with(LocalDensity.current) { itemWidthDp.dp.toPx() }
  val finalImgHeightPx: Float

  when {
    imgWidth > imgHeight -> {
      finalImgHeightPx =
        when {
          imgWidth != itemWidthPx -> {
            val scale = itemWidthPx / imgWidth
            if (imgHeight * scale < itemWidthPx / 3) itemWidthPx / 3 else imgHeight * scale
          }
          else -> {
            if (imgHeight < itemWidthPx / 3) itemWidthPx / 3 else imgHeight
          }
        }
    }
    imgHeight > imgWidth -> {
      finalImgHeightPx =
        when {
          imgWidth != itemWidthPx -> {
            val scale = itemWidthPx / imgWidth
            if (imgHeight * scale > 2 * itemWidthPx) 2 * itemWidthPx else imgHeight * scale
          }
          else -> {
            if (imgHeight > 2 * itemWidthPx) 2 * itemWidthPx else imgHeight
          }
        }
    }
    else -> {
      finalImgHeightPx = itemWidthPx
    }
  }

  val finalImgHeightDp: Dp = with(LocalDensity.current) { finalImgHeightPx.toDp() }

  Box(
    modifier =
      modifier.width(itemWidthDp.dp).height(finalImgHeightDp).clickable {
        if (!item.urls.original.isNullOrEmpty()) {
          onClickImg.invoke(item.urls.original)
        }
      }
  ) {
    SubcomposeAsyncImage(
      modifier = Modifier.fillMaxWidth().fillMaxHeight().clip(RoundedCornerShape(4)),
      model = item.urls.small,
      loading = { ImgStateHint("Loading...") },
      error = { ImgStateHint("Error...") },
      contentDescription = null,
      contentScale = ContentScale.Crop
    )
    Text(
      modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp, 6.dp),
      text = "${item.width} x ${item.height}",
      color = MaterialTheme.colorScheme.primary,
      fontSize = 12.sp,
      style =
        TextStyle(
          shadow =
            Shadow(
              color = MaterialTheme.colorScheme.surfaceTint,
              offset = Offset(x = 2F, y = 2F),
              blurRadius = 3F,
            )
        )
    )
  }
}

@Composable
fun ImgStateHint(hint: String) {
  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = hint,
      fontSize = 12.sp,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.tertiary
    )
  }
}
