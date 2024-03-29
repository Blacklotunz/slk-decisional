package com.slk.presentation;

import com.slk.R;
import com.slk.application.Application;
import com.slk.log.LogHandler;
import com.slk.storage.SLKStorage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ChoiceSupplyActivity extends Activity {

	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;
	private ProgressDialog pd;
	private final Context c= this;
	private Application slk_utility = new Application(c);
	static boolean closeFlag=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogHandler.appendLog(this.toString()+" ChoicheSupplyActivity started");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choicesupply);

		Button green = (Button) findViewById(R.id.under);
		green.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("under supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
				pd = ProgressDialog.show(c,getString(R.string.wait),getString(R.string.retrieving),true,false);
				Handler h = new Handler();
				h.execute(GREEN);
				}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, MainListActivity.class);
					intent.setAction(""+GREEN);
					startActivity(intent);
				}
			}
		});

		Button yellow = (Button) findViewById(R.id.normal);
		yellow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("normal supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
					pd = ProgressDialog.show(c,getString(R.string.wait),getString(R.string.retrieving),true,false);
					Handler h = new Handler();
					h.execute(YELLOW);
					}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, MainListActivity.class);
					intent.setAction(""+YELLOW);
					startActivity(intent);
				}
			}
		});

		Button red = (Button) findViewById(R.id.over);
		red.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("over supply"+" button "+"cliked");
				
				if(slk_utility.getAllProducts().isEmpty()){
					pd = ProgressDialog.show(c,getString(R.string.wait),getString(R.string.retrieving),true,false);
					Handler h = new Handler();
					h.execute(RED);
					}
				else{
					Intent intent = new Intent(ChoiceSupplyActivity.this, MainListActivity.class);
					intent.setAction(""+RED);
					startActivity(intent);
				}
			}
		});

		Button history = (Button) findViewById(R.id.historyc);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("history"+" button "+"cliked");
				
				Intent intent = new Intent(ChoiceSupplyActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume(){
		LogHandler.appendLog("ChoiceSupply"+" Activity "+"resumed");
		
		super.onResume();
		if(ChoiceSupplyActivity.closeFlag){
			MainListActivity.prodotti_selezionati.clear();
			Intent intent = new Intent(this,MainListActivity.class);
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
			slk_utility.setProductsFromWS(false);
			LogHandler.appendLog("setProductsFromWS"+" method "+"called");
			
			Intent intent = new Intent(ChoiceSupplyActivity.this, MainListActivity.class);
			intent.setAction(""+values[0]);
			startActivity(intent);
			return values[0];
		}
		@Override
	      protected void onProgressUpdate(Integer...values) {
	         // aggiorno la progress dialog
	      }
		 @Override
	     protected void onPostExecute(Integer result) {
	        // chiudo la progress dialog
			LogHandler.appendLog("setProductsFromWS"+" data "+"received");
	        pd.dismiss();
	     }
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
			LogHandler.appendLog("setProductsFromWS"+" activity "+"destroyed");
			slk_utility.deleteProductTable();
	}
}
