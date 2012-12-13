package com.slk.presentation;

import java.io.IOException;

import com.slk.R;
import com.slk.application.Application;
import com.slk.log.LogHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceLanguageActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//LogHandler.appendLog(this.toString()+" ChoicheLanguageActivity started");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicelanguage);
		
		//create the Log file where log is saved.
				try {
					LogHandler.createCachedFile(this,"Log.txt","Log created \n ------------------ \n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		//write on log file
		LogHandler.appendLog(this.toString()+" ChoiceLanguageActivity "+"created");
		
		Button eng_button = (Button) findViewById(R.id.english);
		eng_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog(this.toString()+" ChoiceLanguageActivity "+"English language selected");
				Application.local = "en";
				Intent intent = new Intent(ChoiceLanguageActivity.this,Login.class);
				startActivity(intent);
				finish();
			}
		});

		Button sin_button = (Button) findViewById(R.id.sinhala);
		sin_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog(this.toString()+" ChoiceLanguageActivity "+"Sinhala language selected");
				Application.local = "si";
				Intent intent = new Intent(ChoiceLanguageActivity.this,Login.class);
				startActivity(intent);
				finish();
			}
		});

	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		LogHandler.appendLog(this.toString()+" ChoiceLanguageActivity "+"destroyed");
	}
	

}