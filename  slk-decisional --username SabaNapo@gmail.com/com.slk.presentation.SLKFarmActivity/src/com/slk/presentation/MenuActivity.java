package com.slk.presentation;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.slk.R;
import com.slk.http.HttpConnector;
import com.slk.log.LogHandler;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);

		//create the Log file where log is saved. (Now it's created in ChoiceLanguageActivity)
		/*		try {
					LogHandler.createCachedFile(MenuActivity.this, "Log.txt", "Log created \n ------------------ \n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		*/
		//write on log file
		LogHandler.appendLog(this.toString()+" MenuViewActivity "+"created");
		
		Button planning = (Button) findViewById(R.id.cropPlanning);
		planning.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("crop selection"+" button "+"clicked");
				
				Intent intent = new Intent(MenuActivity.this,ChoiceSupplyActivity.class);
				startActivity(intent);
			}
		});

		Button seeding = (Button) findViewById(R.id.seeding);
		seeding.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("seeding"+" button "+"clicked");
				Toast.makeText(MenuActivity.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button planting = (Button) findViewById(R.id.planting);
		planting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("planting"+" button "+"clicked");
				Toast.makeText(MenuActivity.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button growing = (Button) findViewById(R.id.growing);
		growing.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("growing"+" button "+"clicked");
				Toast.makeText(MenuActivity.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button harvesting = (Button) findViewById(R.id.harvesting);
		harvesting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("harvesting"+" button "+"clicked");
				Toast.makeText(MenuActivity.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});

		Button selling = (Button) findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogHandler.appendLog("selling"+" button "+"clicked");
				Toast.makeText(MenuActivity.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
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
			LogHandler.sendMail(MenuActivity.this);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(MenuActivity.this,"Gmail is not available on this device.",Toast.LENGTH_SHORT).show();
		}
	}
	
	/*to remove when relished*/
	static void insertFarm(Context c){
		try {
			final ArrayList<String> farms = new HttpConnector().getFarms();
			final CharSequence[] farmss= {};
			int i = 0;
			for(String s: farms){
				farmss[i]=s;
				i++;
			}
			AlertDialog.Builder alert = new AlertDialog.Builder(c);
			alert.setTitle("choose farm");	
			alert.setSingleChoiceItems(farmss, -1, new DialogInterface.OnClickListener()
		    {
		        public void onClick(DialogInterface dialog, int which) 
		        {
		            HttpConnector.farmId = farms.get(which);
		        }
		    });
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/*To remove when relished*/
	private void insertPin () {
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
	private void insertPhone() {
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
	private void insertFarmId () {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("insert Farm ID");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		alert.setView(input);	
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				HttpConnector.farmId = input.getText().toString();
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

