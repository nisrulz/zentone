package in.excogitation.library_zentone;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.excogitation.zentone.library.ZenTone;


public class MainActivity extends ActionBarActivity {

	EditText txt;
	int freq = 5000;
	ZenTone zenTone = new ZenTone();

	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn1 = (Button) findViewById(R.id.button);
		Button btn2 = (Button) findViewById(R.id.button2);
		txt = (EditText) findViewById(R.id.editText);

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				freq = Integer.parseInt(txt.getText().toString());
				txt.setText(Integer.toString(freq));

				// Use a new tread as this can take a while
				final Thread thread = new Thread(new Runnable() {
					public void run() {
						handler.post(new Runnable() {
							public void run() {
								zenTone.play(freq,5);
							}
						});
					}
				});
				thread.start();
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				zenTone.stop();
			}
		});


	}
}
