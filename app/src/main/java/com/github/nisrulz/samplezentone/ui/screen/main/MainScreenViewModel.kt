package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.lifecycle.ViewModel
import com.github.nisrulz.zentone.ZenTone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel : ViewModel() {

    private val zenTone = ZenTone()

    private var freq = 0f
    private var volume = 0f

    private val _viewState = MutableStateFlow(ViewState(loading = true))
    val viewState = _viewState.asStateFlow()


    init {
        setSuccess(freq, volume)
    }


    fun onPlayStop() {
        if (zenTone.isPlaying) {
            zenTone.stop()
        } else {
            if (volume == 0f || freq == 0f) return
            zenTone.play(freq, volume.toInt())
        }

        setSuccess(freq, volume)
    }

    fun rePlayWithChangedValues() {
        if (zenTone.isPlaying) {
            zenTone.stop()
            if (volume == 0f || freq == 0f) return
            zenTone.play(freq, volume.toInt())
        }
        setSuccess(freq, volume)
    }

    fun setFreq(freq: Float) {
        this.freq = freq
        setSuccess(freq, volume)
    }

    fun setVolume(volume: Float) {
        this.volume = volume
        setSuccess(freq, volume)
    }


    fun release() {
        zenTone.release()
    }

    private fun setSuccess(freq: Float, volume: Float) {
        _viewState.update {
            it.copy(
                loading = false,
                freq = freq,
                volume = volume,
                isPlaying = zenTone.isPlaying
            )
        }
    }
}

data class ViewState(
    val loading: Boolean = false,
    val freq: Float = 0f,
    val volume: Float = 0f,
    val isPlaying: Boolean = false
)