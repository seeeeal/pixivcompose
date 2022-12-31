package com.compose.pixivcompose.ui.module.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import com.compose.pixivcompose.network.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(val imageLoader: ImageLoader, pixivRepository: PixivRepository) : ViewModel() {

  private val _requestState: MutableState<RequestState> = mutableStateOf(RequestState.LOADING)
  val requestState: State<RequestState>
    get() = _requestState

  val randomPicsList: Flow<List<Any>?> =
    pixivRepository.fetchRandomPics(
      onStart = { _requestState.value = RequestState.LOADING },
      onCompletion = { _requestState.value = RequestState.SUCCESS },
      onError = { _requestState.value = RequestState.FAILED }
    )
}
