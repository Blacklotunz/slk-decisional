package com.slk.presentation;

import java.util.ArrayList;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ProductListActivity2 extends ListActivity{

	private ListView listView;
	private ArrayList<Product> prodotti;
	private SLKApplication slk_utility;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		slk_utility = new SLKApplication(getApplicationContext());
		if (getIntent().getAction().equals("1"))
			prodotti = slk_utility.getGreenProducts();
		else if (getIntent().getAction().equals("2"))
			prodotti = slk_utility.getYellowProducts();
		else if (getIntent().getAction().equals("3"))
			prodotti = slk_utility.getRedProducts();
		else prodotti=null;

		Product product_data[] = new Product[prodotti.size()];

		int i = 0;
		for(Product p : prodotti){
			product_data[i]=p;
			i++;
		}


		ProductAdapter adapter = new ProductAdapter(this, R.layout.listview_item_row, product_data);

		listView = (ListView)findViewById(android.R.id.list);	        
		listView.setAdapter(adapter);
		
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) {  
				   Toast.makeText(getBaseContext(), " cliked" + prodotti.get(position).toString(), 1).show();
		        }
		       
		      });  

	}
	
}
