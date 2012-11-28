package com.slk.presentation;



import java.util.ArrayList;
import com.slk.R;
import com.slk.application.DialogBuilder;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import com.slk.log.LogHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class SLKFarmActivity extends TabActivity {

	protected static ArrayList<Product> prodotti_selezionati;
	protected static Button confrontaButton;
	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;
	Bundle savedInstanceState;
	private SLKApplication slk_utility;
	ArrayList<Product> prodotti;
	Context context;


	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogHandler.appendLog("SLKFarmActivity"+" activity "+"created");
		super.onCreate(savedInstanceState);

		ChoiceSupplyActivity.closeFlag=false;

		context = this;
		prodotti_selezionati = new ArrayList<Product>();

		this.savedInstanceState = savedInstanceState;

		setContentView(R.layout.decidinglist);

		//SLKStorage db = new SLKStorage(getApplicationContext());
		//db.clear();


		slk_utility = new SLKApplication(getApplicationContext());
		prodotti = slk_utility.getAllProducts();	
		if(prodotti.isEmpty()){
			slk_utility.setProducts();
			prodotti = slk_utility.getAllProducts();
		}


		TabHost tabHost = getTabHost();
		Intent gIntent = new Intent (this, ProductListActivity2.class);
		gIntent.setAction(""+GREEN);
		tabHost.addTab(tabHost.newTabSpec("GREEN").setContent(gIntent).setIndicator(View.inflate(getApplicationContext(), R.layout.greenbutton, null)));
		Intent yIntent = new Intent (this, ProductListActivity2.class);
		yIntent.setAction(""+YELLOW);
		tabHost.addTab(tabHost.newTabSpec("YELLOW").setContent(yIntent).setIndicator(View.inflate(getApplicationContext(), R.layout.yellowbutton, null)));
		Intent rIntent = new Intent (this, ProductListActivity2.class);
		rIntent.setAction(""+RED);
		tabHost.addTab(tabHost.newTabSpec("RED").setContent(rIntent).setIndicator(View.inflate(getApplicationContext(), R.layout.redbutton, null)));

		if(getIntent().getAction()!=null){
			if (getIntent().getAction().equals("1"))
				tabHost.setCurrentTab(0);
			else if (getIntent().getAction().equals("2"))
				tabHost.setCurrentTab(1);
			else if (getIntent().getAction().equals("3"))
				tabHost.setCurrentTab(2);
			else tabHost.setCurrentTab(0);
		}




		Button ricerca = (Button) findViewById(R.id.bottone_ricerca);
		ricerca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("search"+" button "+"clicked");
				createTextSearchDialog();
			}
		});
		confrontaButton = (Button) findViewById(R.id.confronta);
		confrontaButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("compare"+" button "+"clicked");

				if(prodotti_selezionati.size()>0){
					Intent intent = new Intent(SLKFarmActivity.this,CompareActivity.class);
					startActivity(intent);
				}
				else if(prodotti_selezionati.isEmpty()){
					LogHandler.appendLog("compare"+" button "+"clicked");
					DialogBuilder dialogBuilder= new DialogBuilder(context);
					dialogBuilder.createToast(R.string.no_products, "", (Activity)context).show();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume(){
		super.onResume();
		LogHandler.appendLog("SLKFarmActivity"+" activity "+"resumed");
		if(ChoiceSupplyActivity.closeFlag){
			this.finish();
		}
	}


	private void createTextSearchDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.hint3));
		alert.setMessage(getString(R.string.hint4));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			//search algorithm	
			public void onClick(DialogInterface dialog, int whichButton) {

				boolean flag=false;
				String value = input.getText().toString();
				if(!value.contains(" ")){
					Log.i("value-->",value);
					for(int i=0; i<prodotti.size(); i++){
						Product p = prodotti.get(i);
						Log.i("prodott["+i+"]-->",p.getName());
						if(p.getName().equalsIgnoreCase(value)){
							flag = true;
							SLKFarmActivity.prodotti_selezionati.add(p);
						}
					}
				}
				else{	
					value = value.replaceAll(" ","");
					Log.i("value-->",value);
					for(int i=0; i<prodotti.size(); i++){
						Product p = prodotti.get(i);
						String idProductNoSpace = p.getId().replaceAll(" ", ""); 
						Log.i("prodott["+i+"]-->",idProductNoSpace);
						if(idProductNoSpace.equalsIgnoreCase(value)){
							flag = true;
							SLKFarmActivity.prodotti_selezionati.add(p);
						}
					}
				}
				if(flag==false){
					DialogBuilder dialogBuilder = new DialogBuilder(context);
					dialogBuilder.createToast(R.string.productNotFound, "", (Activity)context).show();
				}
				else{
					Intent intent = new Intent(SLKFarmActivity.this, CompareActivity.class);
					intent.putExtra("clear", false);
					startActivity(intent);
				}
			}
		});
		//end of search algorithm

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//	myLog.appendLog("cancel"+" button "+"clicked");
			}
		});
		alert.show();
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy(){
		super.onDestroy();
		LogHandler.appendLog("SLKFarmActivity"+" activity "+"destroyed");
	}

}