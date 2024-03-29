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

import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.github.nisrulz.samplezentone.databinding.ActivityMainBinding
import com.github.nisrulz.zentone.MIN_FREQUENCY
import com.github.nisrulz.zentone.ZenTone
import com.github.nisrulz.zentone.wave_generators.SineWaveGenerator

class MainActivity : AppCompatActivity() {

    private val zenTone = ZenTone()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)

            myFAB.setOnClickListener {
                handlePlayPauseState(binding)
            }
            setupFreqSeekbar(this)
            setupVolumeSeekbar(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        zenTone.release()
    }

    private fun setupFreqSeekbar(binding: ActivityMainBinding) {
        binding.apply {
            seekBarFreq.max = 22000

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBarFreq.min = MIN_FREQUENCY.toInt()
            }

            seekBarFreq.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    editTextFreq.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    stopPlayingAudio(binding)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    handlePlayPauseState(binding)
                }
            })
        }
    }

    private fun setupVolumeSeekbar(binding: ActivityMainBinding) {
        binding.apply {
            seekBarVolume.max = 100

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBarVolume.min = 0
            }

            seekBarVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    editTextVolume.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    stopPlayingAudio(binding)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    handlePlayPauseState(binding)
                }
            })
        }
    }

    private fun stopPlayingAudio(binding: ActivityMainBinding) {
        binding.apply {
            zenTone.stop()
            myFAB.setImageResource(R.drawable.play)
        }
    }

    private fun handlePlayPauseState(binding: ActivityMainBinding) {
        binding.apply {
            when {
                zenTone.isPlaying -> {
                    stopPlayingAudio(this)
                }
                else -> {
                    val freq = editTextFreq.text.toString().toFloat()
                    val volume = editTextVolume.text.toString().toInt()
                    zenTone.play(
                        frequency = freq,
                        volume = volume,
                        waveByteArrayGenerator = SineWaveGenerator
                    )
                    myFAB.setImageResource(R.drawable.stop)
                }
            }
        }
    }
}