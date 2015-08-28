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

				ZenTone.getInstance().generate(freq,1);

			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ZenTone.getInstance().stop();
			}
		});


	}
}
