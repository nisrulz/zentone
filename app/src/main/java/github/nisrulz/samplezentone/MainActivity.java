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

package github.nisrulz.samplezentone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import static github.nisrulz.zentone.ZenTone.AudioToneListener;
import static github.nisrulz.zentone.ZenTone.getInstance;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

  private EditText editTextFreq;
  private EditText editTextDuration;
  private int freq = 5000;
  private int duration = 1;
  private boolean isPlaying = false;
  AudioToneListener audioToneListener = new AudioToneListener() {
    @Override
    public void onToneStarted() {
      isPlaying = true;
    }

    @Override
    public void onToneStopped() {
      isPlaying = false;
    }
  };
  private FloatingActionButton myFab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    editTextFreq = (EditText) findViewById(R.id.editTextFreq);
    editTextDuration = (EditText) findViewById(R.id.editTextDuration);
    SeekBar seekBarFreq = (SeekBar) findViewById(R.id.seekBarFreq);

    TextView textViewPrivacy = (TextView) findViewById(R.id.textViewPrivacy);
    textViewPrivacy.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Uri uri = Uri.parse(
            "https://cdn.rawgit.com/nisrulz/ae7263ef4f899f5d437f1ffc0b7d910d/raw/5a00042b89b6b730206b0330ad544131fc0d1694/ZentonePrivacyPolicy.html");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
      }
    });
    seekBarFreq.setMax(22000);

    SeekBar seekBarDuration = (SeekBar) findViewById(R.id.seekBarDuration);
    seekBarDuration.setMax(60);

    myFab = (FloatingActionButton) findViewById(R.id.myFAB);
    myFab.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        handleTonePlay();
      }
    });

    seekBarFreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextFreq.setText(String.valueOf(progress));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        getInstance().stop();
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        //Do nothing
      }
    });

    seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextDuration.setText(String.valueOf(progress));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        getInstance().stop();
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        // Do nothing
      }
    });
  }

  private void handleTonePlay() {
    if (isPlaying) {
      getInstance().stop();
    }
    else {
      String freqString = editTextFreq.getText().toString();
      String durationString = editTextDuration.getText().toString();
      freq = Integer.parseInt(freqString);
      duration = Integer.parseInt(durationString);
      // Play Tone
      getInstance().generate(freq, duration, 0.01f, audioToneListener);
    }
  }

  //private void handleTonePlay() {
  //  String freqString = editTextFreq.getText().toString();
  //  String durationString = editTextDuration.getText().toString();
  //  if (!"".equals(freqString) && !"".equals(durationString)) {
  //    if (!isPlaying) {
  //      freq = Integer.parseInt(freqString);
  //      duration = Integer.parseInt(durationString);
  //      // Play Tone
  //      ZenTone.getInstance().generate(freq, duration, 0.01f, new ZenTone.AudioToneListener() {
  //        @Override
  //        public void onToneStarted() {
  //          isPlaying = true;
  //          //myFab.setImageResource(R.drawable.ic_stop_white_24dp);
  //        }
  //
  //        @Override
  //        public void onToneStopped() {
  //          isPlaying = false;
  //          //myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
  //        }
  //      });
  //    }
  //    else {
  //      // Stop Tone
  //      ZenTone.getInstance().stop();
  //      isPlaying = false;
  //      myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
  //    }
  //  }
  //  else if ("".equals(freqString)) {
  //    Toast.makeText(MainActivity.this, "Please enter a frequency!", Toast.LENGTH_SHORT).show();
  //  }
  //  else if ("".equals(durationString)) {
  //    Toast.makeText(MainActivity.this, "Please enter duration!", Toast.LENGTH_SHORT).show();
  //  }
  //}
}
