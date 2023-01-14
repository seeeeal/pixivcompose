package com.compose.pixivcompose.ui.module.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.pixivcompose.ui.module.img.ImgDetail
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PixivRootScreen() {
  val navController = rememberNavController()
  val colors = MaterialTheme.colorScheme

  val systemUiController = rememberSystemUiController()

  var statusBarColor by remember { mutableStateOf(colors.primary) }
  var navigationBarColor by remember { mutableStateOf(colors.primary) }

  val animatedStatusBarColor by animateColorAsState(targetValue = statusBarColor, animationSpec = tween())
  val animatedNavigationBarColor by animateColorAsState(targetValue = navigationBarColor, animationSpec = tween())

  NavHost(navController = navController, startDestination = NavScreen.Home.route) {
    composable(NavScreen.Home.route) {
      HomeScreen(
        viewModel = hiltViewModel(),
        onClickImg = {
          navController.navigate("${NavScreen.ImageDetail.route}?${NavScreen.ImageDetail.argumentUrl}=$it")
        }
      )

      LaunchedEffect(Unit) {
        statusBarColor = colors.secondary
        navigationBarColor = colors.secondary
      }
    }
    composable(
      route = NavScreen.ImageDetail.routeWithArgument,
      arguments = listOf(navArgument(NavScreen.ImageDetail.argumentUrl) { defaultValue = "" })
    ) { backStackEntry ->
      val url = backStackEntry.arguments?.getString(NavScreen.ImageDetail.argumentUrl) ?: ""
      ImgDetail(url = url) { navController.navigateUp() }

      LaunchedEffect(Unit) {
        statusBarColor = colors.secondary
        navigationBarColor = colors.secondary
      }
    }
  }

  LaunchedEffect(animatedStatusBarColor, animatedNavigationBarColor) {
    systemUiController.setStatusBarColor(animatedStatusBarColor)
    systemUiController.setNavigationBarColor(animatedNavigationBarColor)
  }
}

sealed class NavScreen(val route: String) {

  object Home : NavScreen("home")

  object ImageDetail : NavScreen("img_detail") {

    const val routeWithArgument: String = "img_detail?url={url}"

    const val argumentUrl: String = "url"
  }
}
