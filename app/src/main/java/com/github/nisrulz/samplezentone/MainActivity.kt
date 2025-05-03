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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.nisrulz.samplezentone.ui.screen.main.MainScreen
import com.github.nisrulz.samplezentone.ui.screen.main.MainScreenViewModel
import com.github.nisrulz.samplezentone.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

                    MainScreen(
                        viewState = viewState,
                        onFabClick = {
                            viewModel.onPlayStop()
                        }, onVolumeChange = {
                            viewModel.setVolume(it)
                        }, onFreqChange = {
                            viewModel.setFreq(it)
                        }, onValueChangeFinished = {
                            viewModel.rePlayWithChangedValues()
                        })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}


