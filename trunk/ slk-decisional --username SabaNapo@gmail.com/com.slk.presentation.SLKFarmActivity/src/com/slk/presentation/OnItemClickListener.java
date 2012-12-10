package com.slk.presentation;

import com.slk.application.DialogBuilder;
import com.slk.bean.Product;
import com.slk.log.LogHandler;
import com.slk.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class OnItemClickListener implements OnClickListener {
	private int mPosition;
	private Product[]data;
	private CheckBox cb;
	private Context context;
	OnItemClickListener(int position,Product[]data, CheckBox cb, Context context){
		mPosition = position;
		this.data=data;
		this.cb=cb;
		this.context = context;
	}

	public void onClick(View w) {

		if(cb.isChecked())
			cb.setChecked(false);
		else
			cb.setChecked(true);

		if(cb.isChecked()) {
			LogHandler.appendLog(data[mPosition].getId()+" product button "+"selected");
			MainListActivity.prodotti_selezionati.add(data[mPosition]);
		}
		else{
			for(int j=0; j<MainListActivity.prodotti_selezionati.size(); j++){
				if(MainListActivity.prodotti_selezionati.get(j).getId()==data[mPosition].getId()){
					MainListActivity.prodotti_selezionati.remove(j);
				}
			}
		}
		//change the text and the listener of button compare when the number of products change.
		//if there's no product selected
		if(MainListActivity.prodotti_selezionati.size()==0){
			MainListActivity.confrontaButton.setText(context.getString(R.string.confronta));		
			MainListActivity.confrontaButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					LogHandler.appendLog("compare"+" button "+"clicked");
					DialogBuilder dialogBuilder= new DialogBuilder(context);
					dialogBuilder.createToast(R.string.no_products, "", (Activity)context).show();
				}
			});
		}

		//if just ONE product is selected the function of button is show the details of products.
		else if(MainListActivity.prodotti_selezionati.size()==1){
			MainListActivity.confrontaButton.setText(context.getString(R.string.select));
			MainListActivity.confrontaButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					LogHandler.appendLog("select"+" button "+"clicked");
					Intent intent = new Intent(context,DetailActivity.class);
					//intent.putExtra("prodotti_selezionati",SLKFarmActivity.prodotti_selezionati);
					context.startActivity(intent);
				}
			});
		}
		//if MORE than ONE product is selected the function is show compare
		else{
			MainListActivity.confrontaButton.setText(context.getString(R.string.confronta));
			MainListActivity.confrontaButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					LogHandler.appendLog("compare"+" button "+"clicked");
					Intent intent = new Intent(context,CompareActivity.class);
					context.startActivity(intent);
				}
			});
		}
	}      
}
