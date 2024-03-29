package com.slk.presentation;

import java.util.ArrayList;
import java.util.Stack;

import com.slk.R;
import com.slk.application.ImageHandler;
import com.slk.application.Application;
import com.slk.bean.HistoryProdotto;
import com.slk.log.LogHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryActivity extends Activity{

	protected static final int HISTORY_HOME = 10;
	protected ArrayList<HistoryProdotto> history_prodotti;
	protected Application slk_utility;

	protected boolean init=true;
	private LinearLayout LL_riga;
	private final int button_dim_sp=25;
	private Button up,down;
	protected Stack<View> invisible_up,invisible_down;
	protected int n_item_visible;
	private static final String TAG="ACTIVITY LISTA PRODOTTI"; 
	private static final int riga_dim_sp=50;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogHandler.appendLog("HistoryActivity"+" activity "+"created");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		history_prodotti = new ArrayList<HistoryProdotto>();
		slk_utility = new Application(getApplicationContext());
		history_prodotti = slk_utility.getHistoryProducts();

		//metodo che crea il layout quando viene premuto il pulsante history nella home page.
		creaLayout(getApplicationContext(), history_prodotti);

		LL_riga=(LinearLayout) findViewById(R.id.LinearLayout_riga);

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
				LogHandler.appendLog("down"+" button "+"clicked");
				
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
				LogHandler.appendLog("up"+" button "+"clicked");
				
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

	}

	private void creaLayout(Context ctx, ArrayList<HistoryProdotto> history_prodotti){

		LinearLayout LL_riga = (LinearLayout) findViewById(R.id.LinearLayout_riga);
		LinearLayout LL_anno = null;
		LinearLayout LL_img = null;
		LinearLayout LL_info = null;
		LinearLayout LL = null;

		for(HistoryProdotto p : history_prodotti){
			LL = new LinearLayout(getApplicationContext());
			LL.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, convertToSpInpixel(riga_dim_sp)));
			LL.setOrientation(LinearLayout.HORIZONTAL);
			LL.setBackgroundColor(p.getColore());
			LL.setPadding(2, 2, 2, 1);
			LL.setBackgroundResource(R.drawable.rounded_edittext);

			//set year
			LL_anno = new LinearLayout(HistoryActivity.this);
			LinearLayout.LayoutParams lp_anno = new LinearLayout.LayoutParams(convertToSpInpixel(riga_dim_sp), LayoutParams.FILL_PARENT);
			LL_anno.setLayoutParams(lp_anno);
			LL_anno.setBackgroundColor(p.getColore());

			TextView txt_anno = new TextView(HistoryActivity.this);
			txt_anno.setText(""+p.getAnno());
			txt_anno.setGravity(Gravity.CENTER_HORIZONTAL);
			txt_anno.setGravity(Gravity.CENTER_VERTICAL);
			txt_anno.setTextAppearance(getApplicationContext(), R.style.ButtonTextMedium);
			txt_anno.setTextColor(Color.BLACK);
			LL_anno.addView(txt_anno);
			LL.addView(LL_anno);

			//image
			LL_img = new LinearLayout(HistoryActivity.this);
			LinearLayout.LayoutParams lp_img = new LinearLayout.LayoutParams(convertToSpInpixel(riga_dim_sp),convertToSpInpixel(riga_dim_sp));
			LL_img.setLayoutParams(lp_img);
			LL_img.setBackgroundColor(p.getColore());
			ImageView img = new ImageView(HistoryActivity.this);
			
			//int resId = getResources().getIdentifier(p.getNome(), "drawable", getPackageName());
			//img.setImageResource(resId);
			Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImage(this, p.getId()).getAbsolutePath());
			img.setImageBitmap(bitmap);
			
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			LL_img.addView(img);
			LL.addView(LL_img);

			//info
			LL_info = new LinearLayout(HistoryActivity.this);
			LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			LL_info.setLayoutParams(lp_info);
			LL_info.setOrientation(LinearLayout.VERTICAL);
			LL_info.setBackgroundColor(p.getColore());
			TextView txt_nome = new TextView(HistoryActivity.this);
			txt_nome.setText(p.getNome());
			txt_nome.setGravity(Gravity.CENTER);
			txt_nome.setTextAppearance(getApplicationContext(), R.style.ButtonTextMedium);
			txt_nome.setTextColor(Color.BLACK);
			LL_info.addView(txt_nome);

			TextView txt_quantita_anno_corrente = new TextView(HistoryActivity.this);
			txt_quantita_anno_corrente.setText(getString(R.string.planned)+": "+p.getQ_prodotta()+getString(R.string.kg)+".");
			txt_quantita_anno_corrente.setGravity(Gravity.CENTER);
			txt_quantita_anno_corrente.setTextAppearance(getApplicationContext(), R.style.ButtonTextSmall);
			txt_quantita_anno_corrente.setTextColor(Color.BLACK);
			LL_info.addView(txt_quantita_anno_corrente);
			LL.addView(LL_info);

			LL_riga.addView(LL,0);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(init && !history_prodotti.isEmpty())
			setVisibleRow();
		init=false;
	}

	public int convertToSpInpixel(int sp) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().scaledDensity;
		// Convert the dps to pixels, based on density scale
		return (int) (scale*sp);
	}

	private void setVisibleRow(){

		n_item_visible=LL_riga.getHeight()/convertToSpInpixel(riga_dim_sp);
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
	
	@Override
	public void onDestroy(){
		super.onDestroy();
			LogHandler.appendLog("HistoryActivity"+" activity "+"destroyed");
	}
}
