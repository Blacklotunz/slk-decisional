package com.slk.presentation;

import java.util.ArrayList;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import com.slk.storage.SLKStorage;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class SLKFarmActivity extends TabActivity {

	protected static ArrayList<Product> prodotti_selezionati=new ArrayList<Product>();
	protected static Button confrontaButton;
	protected static boolean closeFlag = false;
	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;
	Bundle savedInstanceState;


	private SLKApplication slk_utility;
	ArrayList<Product> prodotti;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SLKFarmActivity.closeFlag=false;
		this.savedInstanceState = savedInstanceState;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decidinglist);
		
		//SLKStorage db = new SLKStorage(getApplicationContext());
		//db.clear();

		slk_utility = new SLKApplication(getApplicationContext());	
		
		//slk_utility.setProducts();

		prodotti=slk_utility.getAllProducts();
		TabHost tabHost = getTabHost();
		Intent gIntent = new Intent (this, ProductListActivity.class);
		gIntent.setAction(""+GREEN);
		tabHost.addTab(tabHost.newTabSpec("GREEN").setContent(gIntent).setIndicator(View.inflate(getApplicationContext(), R.layout.greenbutton, null)));
		Intent yIntent = new Intent (this, ProductListActivity.class);
		yIntent.setAction(""+YELLOW);
		tabHost.addTab(tabHost.newTabSpec("YELLOW").setContent(yIntent).setIndicator(View.inflate(getApplicationContext(), R.layout.yellowbutton, null)));
		Intent rIntent = new Intent (this, ProductListActivity.class);
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
				createTextSearchDialog();
			}
		});

		
		confrontaButton = (Button) findViewById(R.id.confronta);
		confrontaButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(prodotti_selezionati.size()>0){
					Intent intent = new Intent(SLKFarmActivity.this,CompareActivity.class);
					intent.putParcelableArrayListExtra("prodotti_selezionati",prodotti_selezionati);
					startActivity(intent);
				}
				else{
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
					ImageView image = (ImageView) layout.findViewById(R.id.image);
					image.setImageResource(R.drawable.warning);
					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("No products selected! You must select at least one product!");
					text.setGravity(Gravity.CENTER_VERTICAL);
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(layout);
					toast.show();
				}
			}
		});
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(closeFlag)
			finish();
	}
	
	
	private void createTextSearchDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Type Crop To Search");
		alert.setMessage("e.g., banana");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				boolean flag=false;
				String value = input.getText().toString();
				value = value.toLowerCase();
				for(int i=0; i<prodotti.size(); i++){
					if(prodotti.get(i).getNome().toLowerCase().equals(value)){
						flag = true;
						Product p = prodotti.get(i);
						Intent intent = new Intent(SLKFarmActivity.this, DetailActivity.class);
						intent.putExtra("prodotto", p);
						startActivity(intent);
						finish();
					}
				}
				if(flag==false){
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
					ImageView image = (ImageView) layout.findViewById(R.id.image);
					image.setImageResource(R.drawable.warning);
					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("Product not found!");
					text.setGravity(Gravity.CENTER_VERTICAL);
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(layout);
					toast.show();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		
		alert.show();
	}
	

	
	/* method for don't give permission to use back button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}*/

	/* method to use for show an advise before exit
	@Override
	public void onBackPressed() {
		Log.i("SLKFARMACTIVITY","back pressed!");
		finish();
	}*/
}