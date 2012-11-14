package com.slk.presentation;


import com.slk.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

