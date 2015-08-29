package in.excogitation.library_zentone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import in.excogitation.zentone.library.ZenTone;


public class MainActivity extends AppCompatActivity {

    EditText editTextFreq, editTextDuration;
    SeekBar seekBarFreq, seekBarDuration;
    int freq = 5000;
    int duration = 1;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFreq = (EditText) findViewById(R.id.editTextFreq);
        editTextDuration = (EditText) findViewById(R.id.editTextDuration);
        seekBarFreq = (SeekBar) findViewById(R.id.seekBarFreq);
        seekBarFreq.setMax(22000);

        seekBarDuration = (SeekBar) findViewById(R.id.seekBarDuration);
        seekBarDuration.setMax(60);

        final FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editTextFreq.getText().toString().equals("")) {
                    if (!isPlaying) {
                        myFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_white_24dp));
                        freq = Integer.parseInt(editTextFreq.getText().toString());
                        duration = Integer.parseInt(editTextDuration.getText().toString());
                        // Play Tone
                        ZenTone.getInstance().generate(freq, duration);
                        isPlaying = true;

                    } else {
                        // Stop Tone
                        ZenTone.getInstance().stop();
                        isPlaying = false;
                        myFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a frequency!", Toast.LENGTH_SHORT).show();
                }
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
                ZenTone.getInstance().stop();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                ZenTone.getInstance().stop();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
