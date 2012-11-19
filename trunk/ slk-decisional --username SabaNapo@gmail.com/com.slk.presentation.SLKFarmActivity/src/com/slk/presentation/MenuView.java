package com.slk.presentation;

import com.slk.R;
import com.slk.log.LogHandler;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuView extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"created");
		
		setContentView(R.layout.menuview);

		Button planning = (Button) findViewById(R.id.cropPlanning);
		planning.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("crop selection"+" button "+"clicked");

				Intent intent = new Intent(MenuView.this,ChoiceSupplyActivity.class);
				startActivity(intent);
			}
		});

		Button seeding = (Button) findViewById(R.id.seeding);
		seeding.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("seeding"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button planting = (Button) findViewById(R.id.planting);
		planting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("planting"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button growing = (Button) findViewById(R.id.growing);
		growing.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("growing"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button harvesting = (Button) findViewById(R.id.harvesting);
		harvesting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("harvesting"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button selling = (Button) findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("selling"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
	}



	public void onResume(Bundle SavedIstanceState){
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"resumed");
		super.onResume();
	}

	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("resultFromMail-->","request: "+requestCode+" result: "+resultCode+" Intent: "+data.toString());
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"destroyed");
		LogHandler.appendLog("Application closed! \n ------------------------------ \n");
		String s;
		try {
			//send a mail with log
			Intent i = LogHandler.getMailIntent();
			startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
	}

}

