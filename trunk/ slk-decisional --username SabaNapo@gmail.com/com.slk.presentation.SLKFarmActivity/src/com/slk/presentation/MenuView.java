package com.slk.presentation;

import java.io.IOException;

import com.slk.R;
import com.slk.application.ImageHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		/**
		 * example uses of downloadImageFromUrl method
		 
		try {
			ImageHandler.downloadImageFromUrl("http://www.forux.it/wp-content/uploads/icons/android.jpg", "prova");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*
		*
		*/
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);
		GridView g = (GridView) findViewById(R.id.myGrid);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//insert here the Intent for the Activity related to button
				if(position == 1){
					Intent intent = new Intent(MenuView.this, ChoiceSupplyActivity.class);
					startActivity(intent);
				}
				else{
					Toast.makeText(MenuView.this, "Intent not present. Please give this button an activity to launch:" + position, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}
		public int getCount() {
			return mThumbIds.length;
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
				imageView.setAdjustViewBounds(false);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(0, 0, 0, 0);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}
		private Context mContext;
		private Integer[] mThumbIds = {
				R.drawable.ic_launcher, R.drawable.crop_selection,
				R.drawable.growing, R.drawable.harvest,
				R.drawable.ic_launcher, R.drawable.sell,
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menuview, menu);
		return true;
	}
	
	public void onResume(Bundle SavedIstanceState){
		this.onCreate(SavedIstanceState);
	}

}
