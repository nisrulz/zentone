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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import github.nisrulz.samplezentone.databinding.ActivityMainBinding
import github.nisrulz.zentone.ZenTone
import github.nisrulz.zentone.ZenTone.AudioToneListener

/**
 * The type Main activity.
 */
class MainActivity : AppCompatActivity() {

    private var freq = 5000
    private var duration = 1
    private var isPlaying = false

    private var audioToneListener: AudioToneListener = object : AudioToneListener {
        override fun onToneStarted() {
            isPlaying = true
        }

        override fun onToneStopped() {
            isPlaying = false
        }
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)

            textViewPrivacy.setOnClickListener {
                val uri = Uri.parse(
                    "https://cdn.rawgit.com/nisrulz/ae7263ef4f899f5d437f1ffc0b7d910d/raw/5a00042b89b6b730206b0330ad544131fc0d1694/ZentonePrivacyPolicy.html"
                )
                val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(browserIntent)
            }
            seekBarFreq.max = 22000
            val seekBarDuration = findViewById<View>(R.id.seekBarDuration) as SeekBar
            seekBarDuration.max = 60
            myFAB.setOnClickListener { handleTonePlay(this) }
            seekBarFreq.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    editTextFreq.setText(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // Stop Tone
                    ZenTone.getInstance().stop()
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
                    ZenTone.getInstance().stop()
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
                ZenTone.getInstance().stop()
            } else {
                val freqString = editTextFreq.text.toString()
                val durationString = editTextDuration.text.toString()
                freq = freqString.toInt()
                duration = durationString.toInt()
                // Play Tone
                ZenTone.getInstance().generate(freq, duration, 0.01f, audioToneListener)
            }
        }
    }
}