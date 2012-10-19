package com.slk.presentation;

import java.util.ArrayList;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import com.slk.storage.SLKStorage;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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

	protected static ArrayList<Product> prodotti_selezionati=new ArrayList<Product>();
	static final private int GREEN = 1;
	static final private int YELLOW = 2;
	static final private int RED = 3;



	private SLKApplication slk_utility;
	ArrayList<Product> prodotti;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decidinglist);

		slk_utility = new SLKApplication(getApplicationContext());	
		final EditText campo_cerca = (EditText) findViewById(R.id.campo_ricerca);

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
				boolean flag = false;
				String campo = campo_cerca.getText().toString();
				campo = campo.toLowerCase();
				for(int i=0; i<prodotti.size(); i++){
					if(prodotti.get(i).getNome().toLowerCase().equals(campo)){
						flag = true;
						Product p = prodotti.get(i);
						Intent intent = new Intent(SLKFarmActivity.this, DetailActivity.class);
						intent.putExtra("prodotto", p);
						startActivity(intent);
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

		
		final Button confrontaButton = (Button) findViewById(R.id.confronta);
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

	public void onResume(Bundle SavedIstanceState){
		this.onCreate(SavedIstanceState);
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