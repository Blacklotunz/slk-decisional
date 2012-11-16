package com.slk.presentation;


import java.io.File;

import com.slk.R;
import com.slk.debug.LogHandler;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuView extends Activity {

	public static LogHandler myLog = new LogHandler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MenuView.myLog.appendLog(this.toString()+" MenuViewActivity "+"created");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);

		Button planning = (Button) findViewById(R.id.cropPlanning);
		planning.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("crop selection"+" button "+"clicked");

				Intent intent = new Intent(MenuView.this,ChoiceSupplyActivity.class);
				startActivity(intent);
			}
		});

		Button seeding = (Button) findViewById(R.id.seeding);
		seeding.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("seeding"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button planting = (Button) findViewById(R.id.planting);
		planting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("planting"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button growing = (Button) findViewById(R.id.growing);
		growing.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("growing"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button harvesting = (Button) findViewById(R.id.harvesting);
		harvesting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("harvesting"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button selling = (Button) findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				MenuView.myLog.appendLog("selling"+" button "+"clicked");
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
	}



	public void onResume(Bundle SavedIstanceState){
		MenuView.myLog.appendLog(this.toString()+" MenuViewActivity "+"resumed");
		
		super.onResume();
		this.onCreate(SavedIstanceState);
	}

	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("resultFromMail-->","request: "+requestCode+" result: "+resultCode+" Intent: "+data.toString());
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		MenuView.myLog.appendLog(this.toString()+" MenuViewActivity "+"destroyed");

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sabanapo@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "SLK Log File");
		i.putExtra(Intent.EXTRA_TEXT, "see attached file");
		i.setType("application/txt");
		final File file = new File(Environment.getExternalStorageDirectory(), "SLKlog.txt");
		i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		try {
			startActivityForResult(Intent.createChooser(i, "Send mail..."), RESULT_OK);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

}

