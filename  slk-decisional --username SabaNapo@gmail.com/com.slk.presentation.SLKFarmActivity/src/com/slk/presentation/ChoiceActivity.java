package com.slk.presentation;

import com.slk.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends Activity {
	
	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicesupply);

		Button green = (Button) findViewById(R.id.under);
		green.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoiceActivity.this, SLKFarmActivity.class);
				intent.setAction(""+GREEN);
				startActivity(intent);
			}
		});

		Button yellow = (Button) findViewById(R.id.normal);
		yellow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoiceActivity.this, SLKFarmActivity.class);
				intent.setAction(""+YELLOW);
				startActivity(intent);
			}
		});
		
		Button red = (Button) findViewById(R.id.over);
		red.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoiceActivity.this, SLKFarmActivity.class);
				intent.setAction(""+RED);
				startActivity(intent);
			}
		});
		
		Button history = (Button) findViewById(R.id.historyc);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChoiceActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});
		
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}


}
