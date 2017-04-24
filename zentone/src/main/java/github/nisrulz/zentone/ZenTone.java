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

package github.nisrulz.zentone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

public class ZenTone {
  private static final ZenTone INSTANCE = new ZenTone();
  private AudioToneListener audioToneListener;
  private Thread audioPlayingThread = null;
  private int freqOfTone;
  private int duration;
  private float volume = 0f;
  private boolean shouldContinueProcessingAudio;
  private final Runnable audioPlayRunnable = new Runnable() {
    @Override
    public void run() {
      // Setup thread priority
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

      int sampleRate = 8000;

      double dnumSamples = (double) duration * sampleRate;
      dnumSamples = Math.ceil(dnumSamples);
      int numSamples = (int) dnumSamples;
      double[] sample = new double[numSamples];
      byte[] generatedSnd = new byte[2 * numSamples];

      for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
        sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
      }

      // convert to 16 bit pcm sound array
      // assumes the sample buffer is normalized.
      // convert to 16 bit pcm sound array
      // assumes the sample buffer is normalised.
      int idx = 0;
      int i;

      int ramp = numSamples / 20;  // Amplitude ramp as a percent of sample count

      for (i = 0; i < ramp; ++i) {  // Ramp amplitude up (to avoid clicks)
        // Ramp up to maximum
        final short val = (short) (sample[i] * 32767 * i / ramp);
        // in 16 bit wav PCM, first byte is the low order byte
        generatedSnd[idx++] = (byte) (val & 0x00ff);
        generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
      }

      for (i = ramp; i < numSamples - ramp;
          ++i) {                        // Max amplitude for most of the samples
        // scale to maximum amplitude
        final short val = (short) (sample[i] * 32767);
        // in 16 bit wav PCM, first byte is the low order byte
        generatedSnd[idx++] = (byte) (val & 0x00ff);
        generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
      }

      for (i = numSamples - ramp; i < numSamples; ++i) { // Ramp amplitude down
        // Ramp down to zero
        final short val = (short) (sample[i] * 32767 * (numSamples - i) / ramp);
        // in 16 bit wav PCM, first byte is the low order byte
        generatedSnd[idx++] = (byte) (val & 0x00ff);
        generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
      }

      int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO,
          AudioFormat.ENCODING_PCM_16BIT);
      AudioTrack audioTrack =
          new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
              AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

      if (audioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
        return;
      }

      audioTrack.setNotificationMarkerPosition(numSamples);
      audioTrack.setPlaybackPositionUpdateListener(
          new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onMarkerReached(AudioTrack track) {
              audioToneListener.onToneStopped();
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {
              // nothing to do
            }
          });

      // Sanity Check for max volume, set after write method to handle issue in android
      // v 4.0.3
      float maxVolume = AudioTrack.getMaxVolume();

      if (volume > maxVolume) {
        volume = maxVolume;
      }
      else if (volume < 0) {
        volume = 0;
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        audioTrack.setVolume(volume);
      }
      else {
        audioTrack.setStereoVolume(volume, volume);
      }

      // start playing
      audioTrack.play();
      audioToneListener.onToneStarted();
      shouldContinueProcessingAudio = true;

      while (shouldContinueProcessingAudio) {
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
      }

      // stop recording and release the microphone
      try {
        if (audioTrack != null) {
          audioTrack.stop();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (audioTrack != null) {
          audioTrack.release();
          audioTrack = null;
        }
      }
    }
  };

  private ZenTone() {

  }

  public static ZenTone getInstance() {
    return INSTANCE;
  }

  public void generate(int freqOfTone, int duration, float volume,
      AudioToneListener audioToneListener) {
    this.audioToneListener = audioToneListener;
    this.freqOfTone = freqOfTone;
    this.duration = duration;
    this.volume = volume;
    start();
  }

  void start() {
    if (audioPlayingThread == null) {
      audioPlayingThread = new Thread(audioPlayRunnable);
      audioPlayingThread.start();
    }
    else if (audioPlayingThread.isAlive()) {
      stopThreadAndProcessing();
      audioPlayingThread = new Thread(audioPlayRunnable);
      audioPlayingThread.start();
    }
  }

  private void stopThreadAndProcessing() {
    // Stop audio processing
    shouldContinueProcessingAudio = false;
    // interrupt the thread
    if (audioPlayingThread != null) {
      audioPlayingThread.interrupt();
      audioPlayingThread = null;
    }
  }

  public void stop() {
    stopThreadAndProcessing();
    audioToneListener = null;
  }

  public interface AudioToneListener {

    void onToneStarted();

    void onToneStopped();
  }
}
