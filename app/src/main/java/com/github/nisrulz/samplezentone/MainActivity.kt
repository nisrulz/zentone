/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.samplezentone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.nisrulz.samplezentone.ui.screen.main.Event
import com.github.nisrulz.samplezentone.ui.screen.main.MainScreen
import com.github.nisrulz.samplezentone.ui.screen.main.MainScreenViewModel
import com.github.nisrulz.samplezentone.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = darkColorScheme().background,
                ) {
                    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

                    MainScreen(
                        viewState = viewState,
                        onFabClick = {
                            viewModel.onPlayStop()
                        },
                        onVolumeChange = {
                            viewModel.setVolume(it)
                        },
                        onFreqChange = {
                            viewModel.setFreq(it)
                        },
                        onValueChangeFinished = {
                            viewModel.rePlayWithChangedValues()
                        },
                        snackBarHostState = snackBarHostState,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun CoroutineScope.showSnackBar(snackBarHostState: SnackbarHostState, text: String) {
        this.launch {
            snackBarHostState.showSnackbar(text)
        }
    }
}




