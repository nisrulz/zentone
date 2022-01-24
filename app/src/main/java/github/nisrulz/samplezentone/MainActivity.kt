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

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import github.nisrulz.samplezentone.databinding.ActivityMainBinding
import github.nisrulz.zentone.ZenTone

/**
 * The type Main activity.
 */
class MainActivity : AppCompatActivity() {

    private var freq = 5000
    private var duration = 1
    private var isPlaying = false

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)

            seekBarFreq.max = 22000
            seekBarDuration.max = 60

            myFAB.setOnClickListener {
                isPlaying = !isPlaying
                handleTonePlay(this)
            }


            seekBarFreq.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    editTextFreq.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // Stop Tone
                    ZenTone.stop()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    //Do nothing
                }
            })
            seekBarDuration.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    editTextDuration.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // Stop Tone
                    ZenTone.stop()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // Do nothing
                }
            })
        }
    }

    private fun handleTonePlay(binding: ActivityMainBinding) {
        binding.apply {
            if (isPlaying) {
                ZenTone.stop()
            } else {
                freq = editTextFreq.text.toString().toInt()
                duration = editTextDuration.text.toString().toInt()
                // Play Tone
                ZenTone.generate(freq, duration, 0.02f)
            }
        }
    }
}