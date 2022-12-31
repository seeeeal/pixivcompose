package com.compose.pixivcompose.ui.module.main

import android.util.Log
import androidx.annotation.WorkerThread
import com.compose.pixivcompose.network.PixivService
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class PixivRepository @Inject constructor(private val pixivService: PixivService) {

  @WorkerThread
  fun fetchRandomPics(onStart: () -> Unit, onCompletion: () -> Unit, onError: (String) -> Unit) =
    flow {
        try {
          val response = pixivService.fetchRandomPics()
          if (response.isSuccessful) {
            emit(response.body()?.data)
          } else {
            onError.invoke(response.message())
          }
        } catch (exception: Exception) {
          Timber.d(exception.stackTraceToString())
        }
      }
      .onStart { onStart() }
      .onCompletion { onCompletion() }
      .flowOn(Dispatchers.IO)
}
