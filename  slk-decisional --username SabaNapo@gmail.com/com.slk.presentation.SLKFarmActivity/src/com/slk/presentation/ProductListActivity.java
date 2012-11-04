package com.slk.presentation;

import java.util.ArrayList;
import java.util.Stack;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import android.app.Activity;
import android.content.Context;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductListActivity extends Activity{


	private ArrayList<Product> prodotti;
	private SLKApplication slk_utility;
	//	private static Prodotto prodotto_selezionato;
	protected boolean init=true;
	private LinearLayout LL_riga;
	private final int button_dim_sp=25;
	private Button up,down;
	protected Stack<View> invisible_up,invisible_down;
	protected int sotto_view; 
	protected int n_item_visible=4;
	private static final int riga_dim_sp=50;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);

		slk_utility = new SLKApplication(getApplicationContext());

		if (getIntent().getAction().equals("1"))
			prodotti = slk_utility.getGreenProducts();
		else if (getIntent().getAction().equals("2"))
			prodotti = slk_utility.getYellowProducts();
		else if (getIntent().getAction().equals("3"))
			prodotti = slk_utility.getRedProducts();
		else prodotti=null;

		creaLayout(getApplicationContext(), prodotti);
		LL_riga = (LinearLayout) findViewById(R.id.LinearLayout_riga);

		up=(Button)findViewById(R.id.up);
		down=(Button)findViewById(R.id.down);

		up.getLayoutParams().height=convertToSpInpixel(button_dim_sp);
		up.getLayoutParams().width=convertToSpInpixel(button_dim_sp);

		down.getLayoutParams().height=convertToSpInpixel(button_dim_sp);
		down.getLayoutParams().width=convertToSpInpixel(button_dim_sp);

		up.setVisibility(View.INVISIBLE);
		down.setVisibility(View.INVISIBLE);

		down.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				up.setVisibility(View.VISIBLE);
				View view;
				if(!invisible_down.isEmpty())
				{
					view=LL_riga.getChildAt(0);
					LL_riga.removeView(view);
					LL_riga.addView(invisible_down.pop(), LL_riga.getChildCount());
					invisible_up.push(view);

					if(invisible_down.isEmpty())
						down.setVisibility(View.INVISIBLE);

				}
			}
		});


		up.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				down.setVisibility(View.VISIBLE);

				if(!invisible_up.isEmpty())
				{		
					View view=LL_riga.getChildAt(LL_riga.getChildCount()-1);
					LL_riga.removeView(view);
					LL_riga.addView(invisible_up.pop(),0);
					invisible_down.push(view);


					if(invisible_up.isEmpty())
						up.setVisibility(View.INVISIBLE);
				}
			}
		});

		setVisibleRow();

	}



	public int convertToSpInpixel(int sp) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().scaledDensity;
		// Convert the dps to pixels, based on density scale
		return (int) (scale*sp);
	}


	private void creaLayout(Context ctx, ArrayList<Product> prodotti){

		LinearLayout LL_riga = (LinearLayout) findViewById(R.id.LinearLayout_riga);
		LinearLayout LL_img = null;
		LinearLayout LL_info = null;
		LinearLayout LL_flag = null;
		LinearLayout LL = null;


		for(final Product p : prodotti){

			LL = new LinearLayout(getApplicationContext());

			LL.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			LL.setOrientation(LinearLayout.HORIZONTAL);

			//((LinearLayout.LayoutParams)LL.getLayoutParams()).setMargins(0, 0, 0, convertToSpInpixel(0));
			LL.setPadding(2, 2, 2, 1);

			// Set border for a row
			LL.setBackgroundResource(R.drawable.rounded_edittext);

			//Set image for a row
			LL_img = new LinearLayout(ProductListActivity.this);
			LinearLayout.LayoutParams lp_img = new LinearLayout.LayoutParams(convertToSpInpixel(riga_dim_sp),convertToSpInpixel(riga_dim_sp));
			lp_img.setMargins(convertToSpInpixel(0), convertToSpInpixel(0), 0, convertToSpInpixel(0));
			LL_img.setLayoutParams(lp_img);
			LL_img.setBackgroundColor(p.getColore());

			ImageView img = new ImageView(ProductListActivity.this);

			int resId = getResources().getIdentifier(p.getNome(), "drawable", getPackageName());
			img.setImageResource(resId);
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			LL_img.addView(img);
			LL.addView(LL_img);

			//set info
			LL_info = new LinearLayout(ProductListActivity.this);
			LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.FILL_PARENT,(float)0.8);
			lp_info.setMargins(0, convertToSpInpixel(0), 0, convertToSpInpixel(0));
			LL_info.setLayoutParams(lp_info);
			LL_info.setOrientation(LinearLayout.VERTICAL);
			LL_info.setBackgroundColor(p.getColore());

			TextView txt_nome = new TextView(ProductListActivity.this);
			txt_nome.setText(p.getNome());
			txt_nome.setGravity(Gravity.CENTER);
			txt_nome.setTextAppearance(getApplicationContext(), R.style.ButtonTextMedium);
			txt_nome.setTextColor(getResources().getColor(R.color.Black));
			LL_info.addView(txt_nome);

			TextView txt_price = new TextView(ProductListActivity.this);
			txt_price.setText("Variety: "+p.getVariety());
			txt_price.setGravity(Gravity.CENTER);
			txt_price.setTextAppearance(getApplicationContext(), R.style.ButtonTextSmall);
			txt_price.setTextColor(getResources().getColor(R.color.Black));
			LL_info.addView(txt_price);

			LL.addView(LL_info);

			//set checkbox
			LL_flag = new LinearLayout(ProductListActivity.this);
			LL_flag.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams lp_flag = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT+1);

			LL_flag.setLayoutParams(lp_flag);		
			LL_flag.setBackgroundColor(p.getColore());

			//checkbox listener
			CheckBox cb = new CheckBox(ProductListActivity.this);
			cb.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(((CheckBox) v).isChecked()) {
						SLKFarmActivity.prodotti_selezionati.add(p);
					} 
					else{
						for(int j=0; j<SLKFarmActivity.prodotti_selezionati.size(); j++){
							if(SLKFarmActivity.prodotti_selezionati.get(j).getNome()==p.getNome()){
								SLKFarmActivity.prodotti_selezionati.remove(j);
							}
						}
					}
				}
			});

			LL_flag.addView(cb);

			LL.addView(LL_flag);
			LL_riga.addView(LL,0);

			//Listner of a single ROW
			LL.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent=new Intent(ProductListActivity.this,DetailActivity.class);
					intent.putExtra("prodotto", p);
					startActivity(intent);
					//finish();

				}
			});
		}
	}

	private void setVisibleRow(){

		if(n_item_visible<LL_riga.getChildCount())
			down.setVisibility(View.VISIBLE);
		View view;
		invisible_up=new Stack<View>();
		invisible_down=new Stack<View>();
		while(n_item_visible<LL_riga.getChildCount())
		{
			view=LL_riga.getChildAt(n_item_visible);
			invisible_down.push(view);
			LL_riga.removeView(view);  
		}
	}

	/*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	 */

}
