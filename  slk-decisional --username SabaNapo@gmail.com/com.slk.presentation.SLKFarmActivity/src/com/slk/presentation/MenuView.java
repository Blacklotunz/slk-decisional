package com.slk.presentation;

import java.io.IOException;

import com.slk.R;
import com.slk.http.HttpConnector;
import com.slk.log.LogHandler;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
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
		this.showDialog(1);
		switch (UserChoice.selected){
		case 0 :
			HttpConnector.num="345126789";
			HttpConnector.pin="3214";
			break;
		case 1 :
			HttpConnector.num="123456789";
			HttpConnector.pin="1234";
			break;
		case 2 :
			HttpConnector.num="567123489";
			HttpConnector.pin="2123";
			break;
		}
		/**/
		
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
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return createSigleChoiceDialog();
		default:
			return null;
		}
	}
	
	private final Dialog createSigleChoiceDialog() {
		// Otteniamo il riferimento al Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Impostiamo il titolo, il messaggio ed una icona in chaining
		builder.setTitle("choose user");
		// Impostiamo le scelte singole
		final String[] users = {"User1","User2","User3"};
		builder.setSingleChoiceItems(users, -1,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
							UserChoice.selected = which;
						dialog.dismiss();
					}

				});
		return builder.create();
	}
	/**/
}

