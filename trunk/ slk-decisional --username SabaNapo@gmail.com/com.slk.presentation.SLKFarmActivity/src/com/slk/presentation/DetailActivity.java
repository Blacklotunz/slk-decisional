package com.slk.presentation;

import com.slk.R;
import com.slk.application.ColorSetter;
import com.slk.application.DialogBuilder;
import com.slk.application.ImageHandler;
import com.slk.application.Application;
import com.slk.bean.Product;
import com.slk.log.LogHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailActivity extends Activity {
	private static final String UNDER_SUPPLY_0_11 = "#82FA58";
	private static final String UNDER_SUPPLY_12_22 = "#40FF00";
	private static final String UNDER_SUPPLY_23_33 = "#31B404";

	private static final String NORMAL_SUPPLY_34_45 = "#F7D358";
	private static final String NORMAL_SUPPLY_46_57 = "#FFBF00";
	private static final String NORMAL_SUPPLY_58_67 = "#B18904";

	private static final String OVER_SUPPLY_68_79 = "#FE2E2E";
	private static final String OVER_SUPPLY_80_91 = "#B40404";
	private static final String OVER_SUPPLY_92_100 = "#610B0B";
	private static Product prodotto_selezionato; 

	protected static final int SINGLE_CHOICE_DIALOG = 1;
	private int choice;
	private Double actualPrevisione=0.0;
	private RelativeLayout relLay;
	Application slk_utility;
	private Context context;
	private int index;
	private ProgressDialog pd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogHandler.appendLog(this.toString()+" DetailActivity"+"created");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);

		context = this;
		slk_utility= new Application(context);

		Intent intent = getIntent();
		index = intent.getIntExtra("productIndex", 0);
		prodotto_selezionato = MainListActivity.prodotti_selezionati.get(index);

		relLay=(RelativeLayout)findViewById(R.id.prodotto);
		relLay.setBackgroundResource(R.drawable.rounded_edittext);

		ImageView image=(ImageView) findViewById(R.id.immagine_prodotto);
		Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImage(this, prodotto_selezionato.getId()).getAbsolutePath());
		image.setImageBitmap(bitmap);

		/*
		TextView nome=(TextView)findViewById(R.id.nome);
		nome.setText(prodotto_selezionato.getName().toUpperCase());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			nome.setTextColor(Color.WHITE);

		TextView info1=(TextView)findViewById(R.id.info1);
		info1.setText("Variety: "+prodotto_selezionato.getVariety());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info1.setTextColor(Color.WHITE);

		TextView info2=(TextView)findViewById(R.id.info2);
		info2.setText(getString(R.string.maxProduction)+": "+prodotto_selezionato.getMax_production());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info2.setTextColor(Color.WHITE);

		TextView info3=(TextView)findViewById(R.id.info3);
		info3.setText(getString(R.string.currProduction)+": "+prodotto_selezionato.getCurrent_production());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info3.setTextColor(Color.WHITE);

		TextView info4=(TextView)findViewById(R.id.info4);
		info4.setText(getString(R.string.pergentage)+" : "+prodotto_selezionato.getProductionLevel()+"%"+"\n\n"
				+"Product characteristics"+":"+"\n Color :"+prodotto_selezionato.getColorr()+"\n Size: "
					+prodotto_selezionato.getSize()+"\n Weight: "+prodotto_selezionato.getWeight());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info4.setTextColor(Color.WHITE);
		 */

		TextView prevision = (TextView) findViewById(R.id.prevision);
		prevision.setText(getString(R.string.yProduction)+": "+slk_utility.getCurrentQuantity(prodotto_selezionato.getId()));

		TextView lastYear = (TextView) findViewById(R.id.last);
		lastYear.setText(getString(R.string.lastProduction)+": "+slk_utility.getLastYearQuantity(prodotto_selezionato.getId()));

		relLay.setBackgroundColor(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista()));


		final Button confirmButton = (Button) findViewById(R.id.Conferma);
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				LogHandler.appendLog("Confirm"+" button "+"clicked");
				finish();
			}
		});

		final Button history = (Button) findViewById(R.id.historyd);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("history"+" button "+"clicked");

				Intent intent = new Intent(DetailActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});


		final Button QuantityB = (Button) findViewById(R.id.set_quantity);
		QuantityB.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LogHandler.appendLog("Quantity"+" button "+"clicked");

				createTextChoiceDialog();
			}
		});

	}

	@Override
	public void onResume(){
		super.onResume();
		//force a clear of array of selected products and force the restart of listActivity
		MainListActivity.prodotti_selezionati.clear();
		ChoiceSupplyActivity.closeFlag=true;
		//
		//
		TextView nome=(TextView)findViewById(R.id.nome);
		nome.setText(prodotto_selezionato.getName().toUpperCase());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			nome.setTextColor(Color.WHITE);

		TextView info1=(TextView)findViewById(R.id.info1);
		info1.setText("Variety: "+prodotto_selezionato.getVariety());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info1.setTextColor(Color.WHITE);

		TextView info2=(TextView)findViewById(R.id.info2);
		info2.setText(getString(R.string.maxProduction)+": "+prodotto_selezionato.getMax_production());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info2.setTextColor(Color.WHITE);

		TextView info3=(TextView)findViewById(R.id.info3);
		info3.setText(getString(R.string.currProduction)+": "+prodotto_selezionato.getCurrent_production());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info3.setTextColor(Color.WHITE);

		TextView info4=(TextView)findViewById(R.id.info4);
		info4.setText(getString(R.string.pergentage)+" : "+prodotto_selezionato.getProductionLevel()+"%"+"\n\n"
				+"Product characteristics"+"\n Color: "+prodotto_selezionato.getColorr()+"\n Size: "
				+prodotto_selezionato.getSize()+"\n Weight: "+prodotto_selezionato.getWeight());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info4.setTextColor(Color.WHITE);

	}

	private void inserisciProdottoPianificato(Product prod_selezionato){

		/**
		 * lavorare da qui. Il nuovo algoritmo deve inviare messaggi JSon al backend e memorizzare le risposte.
		 */
		//slk_utility.updateProduct(prod_selezionato.getId(),prod_selezionato.getProductionLevel(),prod_selezionato.getCurrent_production(),actualPrevisione);
		//slk_utility.updateListProduct(prod_selezionato.getId(),prod_selezionato.getLista());
		slk_utility.insertOrUpdateProductInHistory(prod_selezionato.getId(), prod_selezionato.getName(),prod_selezionato.getVariety(), prod_selezionato.getPrice(), prodotto_selezionato.getImg(), prod_selezionato.getColor(), slk_utility.getCurrentYear(), slk_utility.getCurrentMonth(), prod_selezionato.getMax_production(), prod_selezionato.getCurrent_production(), actualPrevisione);

		this.onResume();
	}


	private void createTextChoiceDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.hint1));
		alert.setMessage(getString(R.string.hint2));

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		alert.setView(input);
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				LogHandler.appendLog("edit text OK"+" button "+"clicked");

				String value = input.getText().toString();
				if(value.matches("^[-+]?\\d+(\\.{0,1}(\\d+?))?$")){ //input will accept only digits
					actualPrevisione = Double.parseDouble(value);
					createYesNoDialog().show();
				}
				else{
					DialogBuilder dialogBuilder = new DialogBuilder(context);
					dialogBuilder.createToast(R.string.errnumber, "", (Activity)context).show();
				}
			}
		});

		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				LogHandler.appendLog("edit text Cancel"+" button "+"clicked");
			}
		});
		alert.show();
	}


	public void setSupplyLevel(){
		prodotto_selezionato = slk_utility.insertProductInWS(this.actualPrevisione, prodotto_selezionato.getCropId(), prodotto_selezionato.getCultivarId());
	}


	//check new supply level
	public synchronized void checkSupplyLevel(){
		if (prodotto_selezionato.getProductionLevel()<=33){//green list
			if(prodotto_selezionato.getProductionLevel()<=11){
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_0_11));
				prodotto_selezionato.setColor(Color.parseColor(UNDER_SUPPLY_0_11));		
			}
			if(prodotto_selezionato.getProductionLevel()>=12 && prodotto_selezionato.getProductionLevel()<=22){
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_12_22));
				prodotto_selezionato.setColor(Color.parseColor(UNDER_SUPPLY_12_22));
			}
			if(prodotto_selezionato.getProductionLevel()>=23 && prodotto_selezionato.getProductionLevel()<=33){
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_23_33));
				prodotto_selezionato.setColor(Color.parseColor(UNDER_SUPPLY_23_33));
			}
		}else if (prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=67){//yellow list

			if(prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=45){
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_34_45));
				prodotto_selezionato.setColor(Color.parseColor(NORMAL_SUPPLY_34_45));
			}
			if(prodotto_selezionato.getProductionLevel()>=46 && prodotto_selezionato.getProductionLevel()<=57){
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_46_57));
				prodotto_selezionato.setColor(Color.parseColor(NORMAL_SUPPLY_46_57));
			}
			if(prodotto_selezionato.getProductionLevel()>=58 && prodotto_selezionato.getProductionLevel()<=67){
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_58_67));
				prodotto_selezionato.setColor(Color.parseColor(NORMAL_SUPPLY_58_67));
			}
		}else if(prodotto_selezionato.getProductionLevel()>=68){//red list

			if(prodotto_selezionato.getProductionLevel()>=68 && prodotto_selezionato.getProductionLevel()<=79){
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_68_79));
				prodotto_selezionato.setColor(Color.parseColor(OVER_SUPPLY_68_79));
			}
			if(prodotto_selezionato.getProductionLevel()>=80 && prodotto_selezionato.getProductionLevel()<=91){
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_80_91));
				prodotto_selezionato.setColor(Color.parseColor(OVER_SUPPLY_80_91));
			}
			if(prodotto_selezionato.getProductionLevel()>=92){
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_92_100));
				prodotto_selezionato.setColor(Color.parseColor(OVER_SUPPLY_92_100));
			}
		}
		
		//refresh text
		TextView info3=(TextView)findViewById(R.id.info3);
		info3.setText(getString(R.string.currProduction)+": "+prodotto_selezionato.getCurrent_production());
		//if background color is a dark red set textColor to white
		if(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista())==-10417397)
			info3.setTextColor(Color.WHITE);
		
		TextView prevision = (TextView) findViewById(R.id.prevision);
		prevision.setText(getString(R.string.current)+": "+slk_utility.getCurrentQuantity(prodotto_selezionato.getId())+" Kg");
	}

	private final Dialog createYesNoDialog() {
		// Otteniamo il riferimento al Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Impostiamo il titolo, il messaggio ed una icona in chaining
		builder.setTitle(getString(R.string.warning));
		builder.setMessage(getString(R.string.grow)+": "+actualPrevisione+" "+getString(R.string.kg)+". "+getString(R.string.sure));

		// Impostiamo il pulsante di Yes con il relativo listener
		builder.setPositiveButton(R.string.yes_label,
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				LogHandler.appendLog("yes"+" button "+"selected");

				/**
				 * choice is a simulated data from the backend
				 */
				//choice = (int) (actualPrevisione/10);
				//prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);

				pd = ProgressDialog.show(context,getString(R.string.wait),getString(R.string.retrieving),true,false);
				Handler h = new Handler();
				h.execute();


				//checkSupplyLevel();
				//inserisciProdottoPianificato(prodotto_selezionato);

				ChoiceSupplyActivity.closeFlag=true;
			}

		});

		// Impostiamo il pulsante di No con il relativo listener
		builder.setNegativeButton(R.string.no_label,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				LogHandler.appendLog("no"+" button "+"clicked");

				actualPrevisione=0.0;
			}

		});

		// Ritorniamo l'Alert creato
		return builder.create();

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		LogHandler.appendLog("DetailActivity"+" activity "+"destroyed");
	}

	private class Handler extends AsyncTask<Integer,Integer,Integer>{

		@Override
		protected Integer doInBackground(Integer...values) {
			/*
			 * initialize the products database fetching the products from WS, 
			 * the connection will happen only if the db will be empty.
			 */	
			LogHandler.appendLog("insertProduction"+" method "+"called");
			setSupplyLevel();
			return 0;
		}
		@Override
		protected void onProgressUpdate(Integer...values) {
			// aggiorno la progress dialog
			
		}
		@Override
		protected void onPostExecute(Integer result) {
			checkSupplyLevel();
			inserisciProdottoPianificato(prodotto_selezionato);
			// chiudo la progress dialog
			LogHandler.appendLog("insertProduction"+" data "+"received");
			pd.dismiss();	        
		}
	}


}
