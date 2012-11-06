package com.slk.presentation;

import java.util.ArrayList;
import java.util.Stack;

import com.slk.R;
import com.slk.application.ColorSetter;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
			txt_nome_top.setText(p.getNome());
			
			int resId = getResources().getIdentifier(p.getNome(), "drawable", getPackageName());
			img_top.setImageResource(resId);
			txt_avg_top.setText("Average price: "+p.getPrezzo());
			txt_q_ven_anno_prec_top.setText("Quantity sold last year: "+p.getQ_vend_anno_precedente()+" Kg.");
			txt_q_prev_anno_corr_top.setText("Amount planned this year: "+p.getQ_prev_anno_corrente()+" Kg.");
			LL_top.setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.sfondo_prodotto_top).setBackgroundColor(p.getColore());
			
			//if background color is a dark red set textColor to white
			if(ColorSetter.getColours(p.getProductionLevel(), p.getLista())==-10417397){
				txt_avg_top.setTextColor(Color.WHITE);
				txt_q_ven_anno_prec_top.setTextColor(Color.WHITE);
				txt_q_prev_anno_corr_top.setTextColor(Color.WHITE);
			}
			
			addListenerProdotto(LL_top,p);
		}
		if(products.size()==2){
			p = products.get(1);
			txt_nome_bot.setText(p.getNome());
			int resId = getResources().getIdentifier(p.getNome(), "drawable", getPackageName());
			img_bot.setImageResource(resId);
			txt_avg_bot.setText("Average price: "+p.getPrezzo());
			txt_q_ven_anno_prec_bot.setText("Quantity sold last year: "+p.getQ_vend_anno_precedente()+" Kg.");
			txt_q_prev_anno_corr_bot.setText("Amount planned this year: "+p.getQ_prev_anno_corrente()+" Kg.");
			LL_bot.setVisibility(LinearLayout.VISIBLE);
			findViewById(R.id.sfondo_prodotto_bot).setBackgroundColor(p.getColore());
			
			//if background color is a dark red set textColor to white
			if(ColorSetter.getColours(p.getProductionLevel(), p.getLista())==-10417397){
				txt_avg_bot.setTextColor(Color.WHITE);
				txt_q_ven_anno_prec_bot.setTextColor(Color.WHITE);
				txt_q_prev_anno_corr_bot.setTextColor(Color.WHITE);
			}
			
			addListenerProdotto(LL_bot,p);
		}
		if(products.size()==1){
			LL_bot.setVisibility(LinearLayout.INVISIBLE);
		}
	}


	private void addListenerProdotto(RelativeLayout LL, final Product p) {
		LL.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(CompareActivity.this, DetailActivity.class);
				intent.putExtra("prodotto",p);
				//SLKFarmActivity.prodotti_selezionati.clear();
				startActivity(intent);
				//finish();
			}
		});

	}
}
