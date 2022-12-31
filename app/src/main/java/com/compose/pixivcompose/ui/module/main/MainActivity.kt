package com.compose.pixivcompose.ui.module.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import com.compose.pixivcompose.PixivApp
import com.compose.pixivcompose.ui.theme.PixivcomposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      CompositionLocalProvider(PixivApp.LOCAL_IMG_LOADER_PROVIDER provides viewModel.imageLoader) {
        PixivcomposeTheme { viewModel.randomPicsList.collectAsState(initial = listOf()) }
      }
    }
  }
}
