package com.compose.pixivcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PixivApp : Application() {

  override fun onCreate() {
    super.onCreate()
    APP = this

    Timber.plant(Timber.DebugTree())
  }

  companion object {
    lateinit var APP: Application
  }
}
