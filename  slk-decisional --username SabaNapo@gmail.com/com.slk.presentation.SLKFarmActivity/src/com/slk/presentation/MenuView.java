package com.slk.presentation;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import http.HttpConnector;

import com.slk.R;
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
		setContentView(R.layout.menuview);
		
		Button planning = (Button) findViewById(R.id.cropPlanning);
		planning.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(MenuView.this,ChoiceSupplyActivity.class);
				startActivity(intent);
			}
		});
		
		Button seeding = (Button) findViewById(R.id.seeding);
		seeding.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//for test of httpConnector methods
					HttpConnector http = new HttpConnector();
					try {
						if(http.login("http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/registration", "123456789", "1234")==200){
							JSONObject jsonObj = http.getJson();
							if(jsonObj.getInt("success")==1)
							Log.i("MenuView", "secretCode = "+jsonObj.getJSONObject("user").getJSONObject("farmer").getString("secretkey"));
							else
								Log.e("MenuView", "not successfull operation");
						}
						else{
							Log.e("MenuView", "error in http post request");
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
		});
		
		Button planting = (Button) findViewById(R.id.planting);
		planting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button growing = (Button) findViewById(R.id.growing);
		growing.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button harvesting = (Button) findViewById(R.id.harvesting);
		harvesting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button selling = (Button) findViewById(R.id.selling);
		selling.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch", Toast.LENGTH_SHORT).show();
			}
		});
	}
		
		
	
	public void onResume(Bundle SavedIstanceState){
		this.onCreate(SavedIstanceState);
	}

}

