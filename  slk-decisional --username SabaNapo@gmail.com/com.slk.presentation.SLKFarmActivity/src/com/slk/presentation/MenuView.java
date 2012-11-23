package com.slk.presentation;

import java.io.IOException;

import com.slk.R;
import com.slk.http.HttpConnector;
import com.slk.log.LogHandler;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);

		//create the Log file where log is saved.
		try {
			LogHandler.createCachedFile(MenuView.this, "Log.txt", "Log created \n ------------------ \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//write on log file
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"created");
		
		
		/*To remove when relished*/
		this.insertPin();
		this.insertPhone();
		/**/
		
		Button planning = (Button) findViewById(R.id.cropPlanning);
		planning.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("crop selection"+" button "+"clicked");

				Log.i("debug------------", "pin "+HttpConnector.pin);
				Log.i("debug------------", "phone "+HttpConnector.num);
				
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
		super.onResume();
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"resumed");
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"destroyed");
		LogHandler.appendLog("Application closed! \n ------------------------------ \n");		
		try {
			startActivity(LogHandler.getSendEmailIntent(
					MenuView.this,
					"sabanapo@gmail.com", "Log",
					"See attached", "Log.txt"));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(MenuView.this,"Gmail is not available on this device.",Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	/*To remove when relished*/
	private void insertPin ( ) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("insert pin");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		alert.setView(input);	
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				HttpConnector.pin = input.getText().toString();
	}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
	private void insertPhone( ) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.phone));
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		alert.setView(input);		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				HttpConnector.num = input.getText().toString();
	}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
	/**/


}

