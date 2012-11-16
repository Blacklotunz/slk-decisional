package com.slk.presentation;

import java.util.ArrayList;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import com.slk.storage.SLKStorage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class ChoiceSupplyActivity extends Activity {

	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;
	private ProgressDialog pd;
	private final Context c= this;
	private SLKApplication slk_utility = new SLKApplication(c);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MenuView.myLog.appendLog(this.toString()+" ChoicheSupplyActivity started");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicesupply);
		
		SLKStorage db = new SLKStorage(c);
		db.clear();

		Button green = (Button) findViewById(R.id.under);
		green.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MenuView.myLog.appendLog("under supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
				pd = ProgressDialog.show(c,"Loading...","Connecting...",true,false);
				Handler h = new Handler();
				h.execute(GREEN);
				}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, SLKFarmActivity.class);
					intent.setAction(""+GREEN);
					startActivity(intent);
				}
			}
		});

		Button yellow = (Button) findViewById(R.id.normal);
		yellow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MenuView.myLog.appendLog("normal supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
					pd = ProgressDialog.show(c,"Loading...","Connecting...",true,false);
					Handler h = new Handler();
					h.execute(YELLOW);
					}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, SLKFarmActivity.class);
					intent.setAction(""+YELLOW);
					startActivity(intent);
				}
			}
		});

		Button red = (Button) findViewById(R.id.over);
		red.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MenuView.myLog.appendLog("over supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
					pd = ProgressDialog.show(c,"Loading...","Connecting...",true,false);
					Handler h = new Handler();
					h.execute(RED);
					}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, SLKFarmActivity.class);
					intent.setAction(""+RED);
					startActivity(intent);
				}
			}
		});

		Button history = (Button) findViewById(R.id.historyc);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MenuView.myLog.appendLog("history"+" button "+"cliked");
				
				Intent intent = new Intent(ChoiceSupplyActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume(){
		MenuView.myLog.appendLog("ChoiceSupply"+" Activity "+"resumed");
		
		super.onResume();
		if(SLKFarmActivity.closeFlag){
			Intent intent = new Intent(this,SLKFarmActivity.class);
			startActivity(intent);
		}
			
	}
	
	private class Handler extends AsyncTask<Integer,Integer,Integer>{
		
		@Override
		protected Integer doInBackground(Integer...values) {
			/*
			 * initialize the products database fetching the products from WS, 
			 * the connection will happen only if the db will be empty.
			 */	
			slk_utility.setProductsFromWS();
			MenuView.myLog.appendLog("setProductsFromWS"+" method "+"called");
			
			Intent intent = new Intent(ChoiceSupplyActivity.this, SLKFarmActivity.class);
			intent.setAction(""+values[0]);
			startActivity(intent);
			return values[0];
		}
		@Override
	      protected void onProgressUpdate(Integer...values) {
	         // aggiorno la progress dialog
	         pd.setMessage("Connected!");
	      }
		 @Override
	     protected void onPostExecute(Integer result) {
	        // chiudo la progress dialog
			MenuView.myLog.appendLog("setProductsFromWS"+" data "+"received");
	        pd.dismiss();
	     }
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
			MenuView.myLog.appendLog("setProductsFromWS"+" activity "+"destroyed");
	}
}
