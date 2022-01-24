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
package github.nisrulz.samplezentone

import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import github.nisrulz.samplezentone.databinding.ActivityMainBinding
import github.nisrulz.zentone.MIN_FREQUENCY
import github.nisrulz.zentone.ZenTone

class MainActivity : AppCompatActivity() {

    private val zenTone = ZenTone()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)

            seekBarFreq.max = 22000

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBarFreq.min = MIN_FREQUENCY.toInt()
            }

            myFAB.setOnClickListener {
                handlePlayPauseState(binding)
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

    override fun onDestroy() {
        super.onDestroy()
        zenTone.release()
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
                    zenTone.play(freq, 2)
                    myFAB.setImageResource(R.drawable.stop)
                }
            }
        }
    }
}