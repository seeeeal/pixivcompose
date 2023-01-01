package com.compose.pixivcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PixivApp : Application() {

  override fun onCreate() {
    super.onCreate()
    APP = this
  }

  companion object {
    lateinit var APP: Application
  }
}
