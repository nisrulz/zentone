package in.excogitation.library_zentone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import in.excogitation.zentone.library.ToneStoppedListener;
import in.excogitation.zentone.library.ZenTone;


/**
 * @author Nishant Srivastava
 * @project Zentone
 */
public class MainActivity extends AppCompatActivity {

    private EditText editTextFreq;
    private EditText editTextDuration;
    private int freq = 5000;
    private int duration = 1;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFreq = (EditText) findViewById(R.id.editTextFreq);
        editTextDuration = (EditText) findViewById(R.id.editTextDuration);
        SeekBar seekBarFreq = (SeekBar) findViewById(R.id.seekBarFreq);
        seekBarFreq.setMax(22000);

        SeekBar seekBarDuration = (SeekBar) findViewById(R.id.seekBarDuration);
        seekBarDuration.setMax(60);

        final FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editTextFreq.getText().toString().equals("") && !editTextDuration.getText().toString().equals("")) {
                    if (!isPlaying) {
                        myFab.setImageResource(R.drawable.ic_stop_white_24dp);
                        freq = Integer.parseInt(editTextFreq.getText().toString());
                        duration = Integer.parseInt(editTextDuration.getText().toString());
                        // Play Tone
                        ZenTone.getInstance().generate(freq, duration, new ToneStoppedListener() {
                            @Override
                            public void onToneStopped() {
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
                } else if (editTextFreq.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a frequency!", Toast.LENGTH_SHORT).show();
                } else if (editTextDuration.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter duration!", Toast.LENGTH_SHORT).show();
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
