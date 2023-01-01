package com.compose.pixivcompose.ui.module.main

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import com.compose.pixivcompose.network.request.RequestState
import com.compose.pixivcompose.network.response.ResponsePicBean
import com.compose.pixivcompose.ui.module.PixivRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(val imageLoader: ImageLoader, pixivRepository: PixivRepository) : ViewModel() {

  private val _requestState: MutableState<RequestState> = mutableStateOf(RequestState.LOADING)
  val requestState: State<RequestState>
    get() = _requestState

  val randomPicsList: Flow<MutableList<ResponsePicBean>> =
    pixivRepository.fetchRandomPics(
      onStart = { _requestState.value = RequestState.LOADING },
      onCompletion = { _requestState.value = RequestState.SUCCESS },
      onError = { _requestState.value = RequestState.FAILED }
    )
}
