package com.slk.presentation;

import java.util.ArrayList;
import java.util.Stack;

import com.slk.R;
import com.slk.application.ColorSetter;
import com.slk.application.ImageHandler;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import com.slk.log.LogHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompareActivity extends Activity{


	ArrayList<Product> products_to_insert = new ArrayList<Product>();
	protected ArrayList<Product> sec_prodotti;
	protected SLKApplication slk_utility;
	protected Stack<Product> invisible_up = new Stack<Product>();
	protected Stack<Product> invisible_down = new Stack<Product>();

	protected RelativeLayout LL_top;
	protected RelativeLayout LL_bot;

	protected TextView txt_nome_top;
	protected TextView txt_nome_bot;

	protected ImageView img_top;
	protected ImageView img_bot;

	protected TextView txt_avg_top;
	protected TextView txt_avg_bot;

	protected int cont = 0;
	protected int num_page = 0;

	private TextView txt_q_ven_anno_prec_top;
	private TextView txt_q_ven_anno_prec_bot;

	private TextView txt_q_prev_anno_corr_top;
	private TextView txt_q_prev_anno_corr_bot;
	
	private ArrayList<TextView> vListTop = new ArrayList<TextView>();
	private ArrayList<TextView> vListBot = new ArrayList<TextView>();


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogHandler.appendLog(this.toString()+" ChoicheSupplyActivity "+"started");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confronto);

		slk_utility = new SLKApplication(getApplicationContext());

		if(SLKFarmActivity.prodotti_selezionati.size()>2){
			int i=0;
			for(Product p: SLKFarmActivity.prodotti_selezionati){
				if(i==0 || i==1)
					products_to_insert.add(i, SLKFarmActivity.prodotti_selezionati.get(i));
				else
					invisible_down.push(p);

				i++;
			}
		}
		else
			products_to_insert = SLKFarmActivity.prodotti_selezionati;


		caricaLayout(getApplicationContext(), products_to_insert);

		
		final Button upButton = (Button) findViewById(R.id.up);
		final Button downButton = (Button) findViewById(R.id.down);

		//upButton Listener
		upButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				LogHandler.appendLog("upButton"+" button "+"clicked");
				if(cont>0 && cont<num_page){					
					up_griglia(products_to_insert);					
				}
				downButton.setVisibility(View.VISIBLE);
				if(invisible_up.isEmpty())
					upButton.setVisibility(View.INVISIBLE);
			}
		});

		//downButton Listener
		downButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				LogHandler.appendLog("downButton"+" button "+"clicked");
				
				down_griglia(products_to_insert);
				upButton.setVisibility(View.VISIBLE);
				if(invisible_down.isEmpty())
					downButton.setVisibility(View.INVISIBLE);
			}
		});

		upButton.setVisibility(View.INVISIBLE);
		if(invisible_down.isEmpty())
			downButton.setVisibility(View.INVISIBLE);
		else
			downButton.setVisibility(View.VISIBLE);

	}

	@Override
	public void onResume(){
		LogHandler.appendLog("CompareActivity"+" activity "+"resumed");
		
		super.onResume();
		if(SLKFarmActivity.closeFlag)
			finish();
	}
	
	private void caricaLayout(Context applicationContext, ArrayList<Product> products_to_insert) {
		num_page = SLKFarmActivity.prodotti_selezionati.size()/2;
		if(SLKFarmActivity.prodotti_selezionati.size()%2 > 0)
			num_page = num_page + 1;

		LL_top = (RelativeLayout) findViewById(R.id.Layout_top);
		LL_bot = (RelativeLayout) findViewById(R.id.Layout_bot);

		txt_nome_top = (TextView) findViewById(R.id.txt_nome_top);
		txt_nome_bot = (TextView) findViewById(R.id.txt_nome_bot);

		img_top = (ImageView) findViewById(R.id.image_top);
		img_bot = (ImageView) findViewById(R.id.image_bot);

		txt_avg_top = (TextView) findViewById(R.id.txt_avg_top);
		txt_avg_bot = (TextView) findViewById(R.id.txt_avg_bot);

		txt_q_ven_anno_prec_top = (TextView) findViewById(R.id.q_venduta_anno_prec_top);
		txt_q_ven_anno_prec_bot = (TextView) findViewById(R.id.q_venduta_anno_prec_bot);

		txt_q_prev_anno_corr_top = (TextView) findViewById(R.id.q_prev_anno_corr_top);
		txt_q_prev_anno_corr_bot = (TextView) findViewById(R.id.q_prev_anno_corr_bot);


		riempiGriglia(products_to_insert);
	}

	protected void up_griglia(ArrayList<Product> prodotti) {
		cont = cont-1;
		
		if(products_to_insert.size()==2)
			invisible_down.push(products_to_insert.remove(1));
		invisible_down.push(products_to_insert.remove(0));

		if(!invisible_up.isEmpty())
			products_to_insert.add(0,invisible_up.pop());
		if(!invisible_up.isEmpty())
			products_to_insert.add(1,invisible_up.pop());
		if(!products_to_insert.isEmpty())
			riempiGriglia(products_to_insert);
	}

	protected void down_griglia(ArrayList<Product> prodotti) {
		cont = cont+1;

		if(products_to_insert.size()==2)
			invisible_up.push(products_to_insert.remove(1));
		invisible_up.push(products_to_insert.remove(0));

		if(!invisible_down.isEmpty())
			products_to_insert.add(0,invisible_down.pop());
		if(!invisible_down.isEmpty())
			products_to_insert.add(1,invisible_down.pop());
		if(!products_to_insert.isEmpty())
			riempiGriglia(products_to_insert);
	}

	protected void riempiGriglia(ArrayList<Product> products){
		Product p;

		if(products.size()>0){
			p = products.get(0);
			txt_nome_top.setText(p.getName());
			
			//int resId = getResources().getIdentifier(p.getName(), "drawable", getPackageName());
			//img_top.setImageResource(resId);
			Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImage(this, p.getId()).getAbsolutePath());
			img_top.setImageBitmap(bitmap);
			
			//txt_avg_top.setText("Average price: "+p.getPrice());
			txt_q_ven_anno_prec_top.setText(getString(R.string.qLastYear)+": "+slk_utility.getLastYearQuantity(p.getId()));
			txt_q_prev_anno_corr_top.setText(getString(R.string.yProduction)+": "+p.getCurrent_production());
			LL_top.setVisibility(LinearLayout.VISIBLE);
			View top = findViewById(R.id.sfondo_prodotto_top);
			//set color of  background 
			ColorSetter.setBgColor(p,top);
	
			//if background color is a dark red set textColor to white
			vListTop.add(txt_nome_top);
			vListTop.add(txt_avg_top);
			vListTop.add(txt_q_ven_anno_prec_top);
			vListTop.add(txt_q_prev_anno_corr_top);
			ColorSetter.setWhiteTextCompare(p, vListTop);
			
			addListenerProdotto(top,p);
		}
		if(products.size()==2){
			p = products.get(1);
			txt_nome_bot.setText(p.getName());
			//int resId = getResources().getIdentifier(p.getName(), "drawable", getPackageName());
			//img_bot.setImageResource(resId);
			Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImage(this, p.getId()).getAbsolutePath());
			img_bot.setImageBitmap(bitmap);
			
			//txt_avg_bot.setText("Average price: "+p.getPrice());
			txt_q_ven_anno_prec_bot.setText(getString(R.string.maxProduction)+": "+p.getMax_production());
			txt_q_prev_anno_corr_bot.setText(getString(R.string.currProduction)+": "+p.getCurrent_production());
			LL_bot.setVisibility(LinearLayout.VISIBLE);
			View bot = findViewById(R.id.sfondo_prodotto_bot);
			//set color of  background 
			ColorSetter.setBgColor(p,bot);
			
			//if background color is dark set textColor to white
			vListBot.add(txt_nome_bot);
			vListBot.add(txt_avg_bot);
			vListBot.add(txt_q_ven_anno_prec_bot);
			vListBot.add(txt_q_prev_anno_corr_bot);
			ColorSetter.setWhiteTextCompare(p, vListBot);
			
			addListenerProdotto(bot,p);
		}
		if(products.size()==1){
			LL_bot.setVisibility(LinearLayout.INVISIBLE);
		}
	}


	private void addListenerProdotto(View LL, final Product p) {
		LL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog(p.getId()+" button "+"clicked");

				Intent intent = new Intent(CompareActivity.this, DetailActivity.class);
				intent.putExtra("prodotto",p);
				//SLKFarmActivity.prodotti_selezionati.clear();
				startActivity(intent);
				//finish();
			}
		});

	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
			LogHandler.appendLog(this.toString()+" CompareActivity "+"destroyed");
	}
}
