package com.compose.pixivcompose.ui.module.pics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.compose.pixivcompose.network.request.RequestState
import com.compose.pixivcompose.network.response.ResponsePicBean
import com.compose.pixivcompose.ui.module.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, onClickPic: (Long) -> Unit) {

  val isLoading: RequestState by viewModel.requestState
  val randomPics: List<ResponsePicBean> by viewModel.randomPics.collectAsState()

  LaunchedEffect(true) { viewModel.fetchRandomPics() }

  ConstraintLayout {
    val (body, loading) = createRefs()
    Scaffold(
      modifier = Modifier.constrainAs(body) { top.linkTo(parent.top) },
      containerColor = MaterialTheme.colorScheme.primary,
      topBar = { HomeAppBar() }
    ) { innerPadding ->
      val modifier = Modifier.padding(innerPadding)

      HomePics(
        modifier = modifier,
        pics = randomPics,
        onClickPic = onClickPic,
        onClickRefresh = { viewModel.fetchRandomPics() }
      )

      when (isLoading) {
        RequestState.LOADING -> {
          CircularProgressIndicator(
            modifier =
              Modifier.constrainAs(loading) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
              }
          )
        }
        RequestState.SUCCESS -> {}
        RequestState.FAILED -> {}
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeAppBar() {
  TopAppBar(
    modifier = Modifier.statusBarsPadding().height(48.dp),
    title = {
      Text(
        modifier = Modifier.padding(12.dp),
        text = stringResource(com.compose.pixivcompose.R.string.app_name),
        color = MaterialTheme.colorScheme.surfaceTint,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    },
  )
}
