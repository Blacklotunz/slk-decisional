package com.slk.presentation;

import com.slk.R;
import com.slk.R.layout;
import com.slk.R.menu;
import com.slk.application.ColorSetter;
import com.slk.application.SLKApplication;
import com.slk.bean.Product;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

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
	private Product prodotto_selezionato; 

	protected static final int SINGLE_CHOICE_DIALOG = 1;
	private int choice;
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
		relLay.setBackgroundResource(R.drawable.rounded_edittext);

		ImageView image=(ImageView) findViewById(R.id.immagine_prodotto);
		Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/"+prodotto_selezionato.getId()+".png");
		image.setImageBitmap(bitmap);


		TextView nome=(TextView)findViewById(R.id.nome);
		nome.setText(prodotto_selezionato.getNome().toUpperCase());

		TextView info1=(TextView)findViewById(R.id.info1);
		info1.setText("Variety: "+prodotto_selezionato.getVariety());

		TextView info2=(TextView)findViewById(R.id.info2);
		info2.setText("Last year production: "+prodotto_selezionato.getQ_vend_anno_precedente()+" Kg");

		TextView info3=(TextView)findViewById(R.id.info3);
		info3.setText("Last Year Selling Price: "+prodotto_selezionato.getPrezzo()+" $/Kg");

		TextView info4=(TextView)findViewById(R.id.info4);
		info4.setText("Current production plan: "+slk_utility.getCurrentQuantity(prodotto_selezionato.getId())+" Kg");


		TextView prevision = (TextView) findViewById(R.id.previsione);
		prevision.setText("Your Prevision is: "+slk_utility.getCurrentQuantity(prodotto_selezionato.getId())+" Kg");

		TextView lastYear = (TextView) findViewById(R.id.last);
		lastYear.setText("Last Year Production: "+prodotto_selezionato.getQ_vend_anno_precedente()+" Kg");

		relLay.setBackgroundColor(ColorSetter.getColours(prodotto_selezionato.getProductionLevel(), prodotto_selezionato.getLista()));
		//relLay.setBackgroundColor(prodotto_selezionato.getColore());

		final Button confirmButton = (Button) findViewById(R.id.Conferma);
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this, ChoiceSupplyActivity.class);
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
				createTextChoiceDialog();
			}
		});

	}

	private void inserisciProdottoPianificato(Product prod_selezionato){

		/**
		 * lavorare da qui. Il nuovo algoritmo deve inviare messaggi JSon al backend e memorizzare le risposte.
		 */
		slk_utility.updateProduct(prod_selezionato.getId(),prod_selezionato.getProductionLevel(),prod_selezionato.getQ_prev_anno_corrente(),actualPrevisione);
		slk_utility.updateListProduct(prod_selezionato.getId(),prod_selezionato.getLista());
		slk_utility.insertOrUpdateProductInHistory(prod_selezionato.getId(), prod_selezionato.getNome(),prod_selezionato.getVariety(), prod_selezionato.getPrezzo(), prodotto_selezionato.getImg(), prod_selezionato.getColore(), slk_utility.getCurrentYear(), slk_utility.getCurrentMonth(), prod_selezionato.getQ_vend_anno_precedente(), prod_selezionato.getQ_prev_anno_corrente(), actualPrevisione);
	}


	private void createTextChoiceDialog() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Type Quantity to grow");
		alert.setMessage("e.g., 123.4");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				if(value.matches("^[-+]?\\d+(\\.{0,1}(\\d+?))?$")){ //input will accept only digits
					actualPrevisione = Double.parseDouble(value);
					createYesNoDialog().show();
				}
				else{
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));
					ImageView image = (ImageView) layout.findViewById(R.id.image);
					image.setImageResource(R.drawable.warning);
					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("Wrong number format.");
					text.setGravity(Gravity.CENTER_VERTICAL);
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(layout);
					toast.show();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	//check supplyLevel for change background color
	public void checkAndSetSupplyLevel(){
		if (prodotto_selezionato.getProductionLevel()<=33){//green list
			prodotto_selezionato.setLista(1);
			if(prodotto_selezionato.getProductionLevel()<=11){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_0_11));
				
				prodotto_selezionato.setColore(Color.parseColor(UNDER_SUPPLY_0_11));		
			}
			if(prodotto_selezionato.getProductionLevel()>=12 && prodotto_selezionato.getProductionLevel()<=22){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_12_22));
				prodotto_selezionato.setColore(Color.parseColor(UNDER_SUPPLY_12_22));
			}
			if(prodotto_selezionato.getProductionLevel()>=23 && prodotto_selezionato.getProductionLevel()<=33){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(UNDER_SUPPLY_23_33));
				prodotto_selezionato.setColore(Color.parseColor(UNDER_SUPPLY_23_33));
			}
		}else if (prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=67){//yellow list
			prodotto_selezionato.setLista(2);
			if(prodotto_selezionato.getProductionLevel()>=34 && prodotto_selezionato.getProductionLevel()<=45){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_34_45));
				prodotto_selezionato.setColore(Color.parseColor(NORMAL_SUPPLY_34_45));
			}
			if(prodotto_selezionato.getProductionLevel()>=46 && prodotto_selezionato.getProductionLevel()<=57){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_46_57));
				prodotto_selezionato.setColore(Color.parseColor(NORMAL_SUPPLY_46_57));
			}
			if(prodotto_selezionato.getProductionLevel()>=58 && prodotto_selezionato.getProductionLevel()<=67){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(NORMAL_SUPPLY_58_67));
				prodotto_selezionato.setColore(Color.parseColor(NORMAL_SUPPLY_58_67));
			}
		}else if(prodotto_selezionato.getProductionLevel()>=68){//red list
			prodotto_selezionato.setLista(3);
			if(prodotto_selezionato.getProductionLevel()>=68 && prodotto_selezionato.getProductionLevel()<=79){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_68_79));
				prodotto_selezionato.setColore(Color.parseColor(OVER_SUPPLY_68_79));
			}
			if(prodotto_selezionato.getProductionLevel()>=80 && prodotto_selezionato.getProductionLevel()<=91){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_80_91));
				prodotto_selezionato.setColore(Color.parseColor(OVER_SUPPLY_80_91));
			}
			if(prodotto_selezionato.getProductionLevel()>=92){
				prodotto_selezionato.setProductionLevel(prodotto_selezionato.getProductionLevel()+choice);
				relLay.setBackgroundColor(Color.parseColor(OVER_SUPPLY_92_100));
				prodotto_selezionato.setColore(Color.parseColor(OVER_SUPPLY_92_100));
			}
		}
	}

	private final Dialog createYesNoDialog() {
		// Otteniamo il riferimento al Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Impostiamo il titolo, il messaggio ed una icona in chaining
		builder.setTitle(R.string.warning);
		builder.setMessage("You have selected to grow "+actualPrevisione+"Kg. Are you sure?");

		// Impostiamo il pulsante di Yes con il relativo listener
		builder.setPositiveButton(R.string.yes_label,
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				/**
				 * choice is a simulated data from the backend
				 */
				choice = (int) (actualPrevisione/10);
				checkAndSetSupplyLevel(); //change the background color and set the supply level into the bean
				inserisciProdottoPianificato(prodotto_selezionato);
			}

		});

		// Impostiamo il pulsante di No con il relativo listener
		builder.setNegativeButton(R.string.no_label,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				actualPrevisione=0.0;
			}

		});

		TextView prevision = (TextView) findViewById(R.id.previsione);
		prevision.setText("Your Prevision is: "+(prodotto_selezionato.getQ_prev_anno_corrente()+actualPrevisione));

		// Ritorniamo l'Alert creato
		return builder.create();

	}

}
