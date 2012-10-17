package com.slk.presentation;

import java.util.ArrayList;

import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity{

	private Product prodotto_selezionato; 

	protected static final int SINGLE_CHOICE_DIALOG = 1;
	private int oldPrevisione=0;
	private int actualPrevisione=0;
	private RelativeLayout relLay;
	SLKApplication slk_utility;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		
		slk_utility= new SLKApplication(getApplicationContext());
		prodotto_selezionato=getIntent().getExtras().getParcelable("prodotto");
		relLay=(RelativeLayout)findViewById(R.id.prodotto);
		
		
		ImageView image=(ImageView) findViewById(R.id.immagine_prodotto);
		int resId = getResources().getIdentifier(prodotto_selezionato.getNome(), "drawable", getPackageName());
		image.setImageResource(resId);

		TextView nome=(TextView)findViewById(R.id.nome);
		nome.setText(prodotto_selezionato.getNome().toUpperCase());
		
		TextView info1=(TextView)findViewById(R.id.info1);
		info1.setText("Last Year Avg Price: "+prodotto_selezionato.getPrezzo()+"$");
		
		TextView info2=(TextView)findViewById(R.id.info2);
		info2.setText("Last Year Production: "+prodotto_selezionato.getQ_vend_anno_precedente()+" Kg.");

		TextView info3=(TextView)findViewById(R.id.info3);
		info3.setText("Current Production Plan: "+prodotto_selezionato.getQ_prev_anno_corrente()+" Kg.");
		
		TextView info4=(TextView)findViewById(R.id.info4);
		

		TextView prevision = (TextView) findViewById(R.id.previsione);
		prevision.setText("Your Prevision is: "+ slk_utility.getCurrentQuantity(prodotto_selezionato.getNome()));

		relLay.setBackgroundColor(Color.GREEN);
		if(prodotto_selezionato.getLista()==2){
			relLay.setBackgroundColor(Color.YELLOW);
		}else if(prodotto_selezionato.getLista()==3){
			relLay.setBackgroundColor(Color.RED);
		}


		//controllo se era già selezionato in precedenza
		if(oldPrevisione>10)
			actualPrevisione=actualPrevisione-oldPrevisione;
		Log.v("previsione", ""+actualPrevisione);


		final Button confirmButton = (Button) findViewById(R.id.Conferma);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				inserisciProdottoPianificato(prodotto_selezionato);
				Intent intent = new Intent(DetailActivity.this, SLKFarmActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		final Button history = (Button) findViewById(R.id.historyd);
		history.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});

		
		final Button QuantityB = (Button) findViewById(R.id.set_quantity);
		QuantityB.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(SINGLE_CHOICE_DIALOG);
			}
		});

	}

	private void inserisciProdottoPianificato(Product prod_selezionato){

		int colore=0;
		if(prodotto_selezionato.getLista()==1)
			colore=Color.GREEN;
		else if(prodotto_selezionato.getLista()==2)
			colore=Color.YELLOW;
		else if(prodotto_selezionato.getLista()==3)
			colore=Color.RED;

		slk_utility.insertOrUpdateProductInHistory(prod_selezionato.getId(), prod_selezionato.getNome(),prod_selezionato.getVariety(), prod_selezionato.getPrezzo(), prodotto_selezionato.getImg(), colore, slk_utility.getCurrentYear(), 5, prod_selezionato.getQ_vend_anno_precedente(), prod_selezionato.getQ_prev_anno_corrente(), actualPrevisione);
		slk_utility.updateProduct(prod_selezionato.getNome(),prod_selezionato.getQ_prev_anno_corrente(),actualPrevisione);
		slk_utility.updateListProduct(prodotto_selezionato.getNome(),prodotto_selezionato.getLista());
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SINGLE_CHOICE_DIALOG:
			return createSigleChoiceDialog();
		default:
			return null;
		}
	}

	private final Dialog createSigleChoiceDialog() {
		// Otteniamo il riferimento al Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Impostiamo il titolo, il messaggio ed una icona in chaining
		builder.setTitle("Select quantity to grow").setIcon(R.id.immagine_prodotto);
		// Impostiamo le scelte singole
		final String[] quantities = getResources().getStringArray(
				R.array.quantities_array);
		builder.setSingleChoiceItems(quantities, -1, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(DetailActivity.this,
						"Selected: " + quantities[which]+" press OK button to save your choice",
						Toast.LENGTH_SHORT).show();
				if(which==0)
					actualPrevisione = 100;
				if(which==1)
					actualPrevisione = 250;
				if(which==2)
					actualPrevisione = 750;
				if(which==3)
					actualPrevisione = 1750;
				if(which==4)
					actualPrevisione = 2750;
				if(which==5)
					actualPrevisione = 5000;
				
				TextView prevision = (TextView) findViewById(R.id.previsione);
				prevision.setText("Your Prevision is: "+actualPrevisione);
				Button QuantityB= (Button) findViewById(R.id.set_quantity);
				QuantityB.setText("Selected "+quantities[which]);
				
				//faccio la differenza per aggiornare colore, lista, ecc.
				int differenza=prodotto_selezionato.getQ_vend_anno_precedente()-(prodotto_selezionato.getQ_prev_anno_corrente()+actualPrevisione);
				if (differenza > 400){//lista verde
					prodotto_selezionato.setLista(1);
					relLay.setBackgroundColor(Color.GREEN);
				}else if (differenza<0){//lista rossa
					prodotto_selezionato.setLista(3);
					relLay.setBackgroundColor(Color.RED);
				}else{//lista gialla
					prodotto_selezionato.setLista(2);
					relLay.setBackgroundColor(Color.YELLOW);
				}

				dialog.dismiss();
			}

		});
		return builder.create();
	}
}

