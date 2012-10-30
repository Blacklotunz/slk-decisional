package com.slk.presentation;


import com.slk.R;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	private static final String UNDER_SUPPLY_0_11 = "#82FA58";
	private static final String UNDER_SUPPLY_12_22 = "#40FF00";
	private static final String UNDER_SUPPLY_23_33 = "#31B404";

	private static final String NORMAL_SUPPLY_34_45 = "#F7D358";
	private static final String NORMAL_SUPPLY_46_57 = "#FFBF00";
	private static final String NORMAL_SUPPLY_58_67 = "#B18904";

	private static final String OVER_SUPPLY_68_79 = "#FE2E2E";
	private static final String OVER_SUPPLY_80_91 = "#B40404";
	private static final String OVER_SUPPLY_92_100 = "#610B0B";
	private Product prodotto_selezionato; 

	protected static final int SINGLE_CHOICE_DIALOG = 1;
	private int choice = -1;
	private Double oldPrevisione=0.0;
	private Double actualPrevisione=0.0;
	private RelativeLayout relLay;
	SLKApplication slk_utility;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);

		slk_utility= new SLKApplication(getApplicationContext());

		Intent intent = getIntent();
		prodotto_selezionato=intent.getParcelableExtra("prodotto");

		relLay=(RelativeLayout)findViewById(R.id.prodotto);


		ImageView image=(ImageView) findViewById(R.id.immagine_prodotto);
		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/"+prodotto_selezionato.getId()+".png");
		image.setImageBitmap(bitmap);


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
		prevision.setText("Your Prevision is: "+ slk_utility.getCurrentQuantity(prodotto_selezionato.getId()));

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
				setSupplyLevel();
				checkSupplyLevel();
				inserisciProdottoPianificato(prodotto_selezionato);
				Intent intent = new Intent(DetailActivity.this, SLKFarmActivity.class);
				SLKFarmActivity.prodotti_selezionati.clear();
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

		int colore= prod_selezionato.getColore();
		/**
		 * lavorare da qui. Il nuovo algoritmo deve inviari messaggi JSon al backend e memorizzare le risposte.
		 */
		slk_utility.insertOrUpdateProductInHistory(prod_selezionato.getId(), prod_selezionato.getNome(),prod_selezionato.getVariety(), prod_selezionato.getPrezzo(), prodotto_selezionato.getImg(), colore, slk_utility.getCurrentYear(), slk_utility.getCurrentMonth(), prod_selezionato.getQ_vend_anno_precedente(), prod_selezionato.getQ_prev_anno_corrente(), actualPrevisione);
		slk_utility.updateProduct(prod_selezionato.getId(),prod_selezionato.getProductionLevel(),prod_selezionato.getQ_prev_anno_corrente(),actualPrevisione);
		slk_utility.updateListProduct(prod_selezionato.getId(),prod_selezionato.getLista());
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

				int pL = prodotto_selezionato.getProductionLevel();

				if(which==0){
					actualPrevisione = 100.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 10;
					//prodotto_selezionato.setProductionLevel(pL+10);
					/**
					 * 
					 */
				}
				if(which==1){
					actualPrevisione = 250.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 20;
					//prodotto_selezionato.setProductionLevel(pL+20);
					/**
					 * 
					 */
				}
				if(which==2){
					actualPrevisione = 750.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 30;
					//prodotto_selezionato.setProductionLevel(pL+30);
					/**
					 * 
					 */
				}
				if(which==3){
					actualPrevisione = 1750.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 40;
					//prodotto_selezionato.setProductionLevel(pL+40);
					/**
					 * 
					 */
				}
				if(which==4){
					actualPrevisione = 2750.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 50;
					//prodotto_selezionato.setProductionLevel(pL+50);
					/**
					 * 
					 */
				}
				if(which==5){
					actualPrevisione = 5000.0;
					/**
					 * to change. get the production level from the json query
					 */
					choice = 60;
					//prodotto_selezionato.setProductionLevel(pL+60);
					/**
					 * 
					 */
				}

				/*if(prodotto_selezionato.getProductionLevel()<=33){
					prodotto_selezionato.setLista(1);
				}
				else if(prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=67){
					prodotto_selezionato.setLista(2);
				}
				else if(prodotto_selezionato.getProductionLevel()>=68){
					prodotto_selezionato.setLista(3);
				}*/


				TextView prevision = (TextView) findViewById(R.id.previsione);
				prevision.setText("Your Prevision is: "+actualPrevisione);
				Button QuantityB= (Button) findViewById(R.id.set_quantity);
				QuantityB.setText("Selected "+quantities[which]);
				dialog.dismiss();
			}

		});
		return builder.create();
	}

	public void checkSupplyLevel(){
		//check supplyLevel for change color
		if (prodotto_selezionato.getProductionLevel()<=33){//green list
			prodotto_selezionato.setLista(1);
			if(prodotto_selezionato.getProductionLevel()<=11)
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_0_11));
			if(prodotto_selezionato.getProductionLevel()>=12 && prodotto_selezionato.getProductionLevel()<=22)
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_12_22));
			if(prodotto_selezionato.getProductionLevel()>=23 && prodotto_selezionato.getProductionLevel()<=33)
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_23_33));
		}else if (prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=67){//yellow list
			prodotto_selezionato.setLista(2);
			if(prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=45)
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_34_45));
			if(prodotto_selezionato.getProductionLevel()>=46 && prodotto_selezionato.getProductionLevel()<=57)
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_46_57));
			if(prodotto_selezionato.getProductionLevel()>=58 && prodotto_selezionato.getProductionLevel()<=67)
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_58_67));
		}else if(prodotto_selezionato.getProductionLevel()>=68){//red list
			prodotto_selezionato.setLista(3);
			if(prodotto_selezionato.getProductionLevel()>=68 && prodotto_selezionato.getProductionLevel()<=79)
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_68_79));
			if(prodotto_selezionato.getProductionLevel()>=80 && prodotto_selezionato.getProductionLevel()<=91)
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_80_91));
			if(prodotto_selezionato.getProductionLevel()>=92)
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_92_100));
		}
	}
	private void setSupplyLevel() {
		prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);	
	}
}

