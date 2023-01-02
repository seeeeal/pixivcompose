package com.compose.pixivcompose.ui.module.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.compose.pixivcompose.network.request.RequestState
import com.compose.pixivcompose.network.response.ResponsePicBean
import com.compose.pixivcompose.ui.module.PixivRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(val imageLoader: ImageLoader, val pixivRepository: PixivRepository) :
  ViewModel() {

  private val _requestState: MutableState<RequestState> = mutableStateOf(RequestState.LOADING)
  val requestState: State<RequestState>
    get() = _requestState

  private val _randomPics = MutableStateFlow(listOf<ResponsePicBean>())
  val randomPics: StateFlow<List<ResponsePicBean>> = _randomPics.asStateFlow()

  fun fetchRandomPics() {
    viewModelScope.launch {
      pixivRepository
        .fetchRandomPics(
          onStart = { _requestState.value = RequestState.LOADING },
          onCompletion = { _requestState.value = RequestState.SUCCESS },
          onError = { _requestState.value = RequestState.FAILED }
        )
        .collect { _randomPics.value = it }
    }
  }
}
