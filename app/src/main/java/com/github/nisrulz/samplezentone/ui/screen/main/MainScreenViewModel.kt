package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MainScreenViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()


}


internal sealed class ViewState {
    data object Loading : ViewState()
    data object Error : ViewState()
    data class Success(val freq: Int) : ViewState()
}