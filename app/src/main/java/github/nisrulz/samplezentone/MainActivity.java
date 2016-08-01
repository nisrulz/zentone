package github.nisrulz.samplezentone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import github.nisrulz.zentone.ToneStoppedListener;
import github.nisrulz.zentone.ZenTone;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

  private EditText editTextFreq;
  private EditText editTextDuration;
  private int freq = 5000;
  private int duration = 1;
  private boolean isPlaying = false;
  private FloatingActionButton myFab;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    editTextFreq = (EditText) findViewById(R.id.editTextFreq);
    editTextDuration = (EditText) findViewById(R.id.editTextDuration);
    SeekBar seekBarFreq = (SeekBar) findViewById(R.id.seekBarFreq);

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
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextFreq.setText(String.valueOf(progress));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        ZenTone.getInstance().stop();
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        //Do nothing
      }
    });

    seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextDuration.setText(String.valueOf(progress));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        ZenTone.getInstance().stop();
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
          // Do nothing
      }
    });
  }

  private void handleTonePlay() {
    String freqString = editTextFreq.getText().toString();
    String durationString = editTextDuration.getText().toString();
    if (!"".equals(freqString) && !"".equals(durationString)) {
      if (!isPlaying) {
        myFab.setImageResource(R.drawable.ic_stop_white_24dp);
        freq = Integer.parseInt(freqString);
        duration = Integer.parseInt(durationString);
        // Play Tone
        ZenTone.getInstance().generate(freq, duration, 0.01f, new ToneStoppedListener() {
          @Override public void onToneStopped() {
            isPlaying = false;
            myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
          }
        });
        isPlaying = true;
      } else {
        // Stop Tone
        ZenTone.getInstance().stop();
        isPlaying = false;
        myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
      }
    } else if ("".equals(freqString)) {
      Toast.makeText(MainActivity.this, "Please enter a frequency!", Toast.LENGTH_SHORT).show();
    } else if ("".equals(durationString)) {
      Toast.makeText(MainActivity.this, "Please enter duration!", Toast.LENGTH_SHORT).show();
    }
  }
}
