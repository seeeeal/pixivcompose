package com.compose.pixivcompose

import android.app.Application
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PixivApp : Application() {

  override fun onCreate() {
    super.onCreate()
    APP = this
    LOCAL_IMG_LOADER_PROVIDER = compositionLocalOf { ImageLoader(APP) }
  }

  companion object {
    lateinit var APP: Application
    lateinit var LOCAL_IMG_LOADER_PROVIDER: ProvidableCompositionLocal<ImageLoader>
  }
}
