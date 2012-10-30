package com.slk.application;

import java.util.ArrayList;

import com.slk.bean.Product;

import android.graphics.Color;


public class ColorSetter {

	private static final String UNDER_SUPPLY_0_11 = "#82FA58";
	private static final String UNDER_SUPPLY_12_22 = "#40FF00";
	private static final String UNDER_SUPPLY_23_33 = "#31B404";

	private static final String NORMAL_SUPPLY_34_45 = "#F7D358";
	private static final String NORMAL_SUPPLY_46_57 = "#FFBF00";
	private static final String NORMAL_SUPPLY_58_67 = "#B18904";

	private static final String OVER_SUPPLY_68_79 = "#FE2E2E";
	private static final String OVER_SUPPLY_80_91 = "#B40404";
	private static final String OVER_SUPPLY_92_100 = "#610B0B";

	/*
	 * 
	 * ISTRUZIONI
	 * chiamare la funzione getColours(x, y)
	 * con x: numero di colori che si vogliono ottenere
	 * y: codice del colore {1,2,3} 1 = verde, 2 = giallo, 3 = rosso
	 * 
	 */
	/*
	public static int[] getColours(int num_col, int color_code) {
		int[] colours = new int[num_col];
		int percentuale = 185/num_col; //185 per non prendere colori troppo chiari (troppo vicini a 255)
		if (color_code == 1) { //codice per il verde
			for (int i = 0; i < num_col; i++) {
				int brighter = Color.rgb(percentuale * i, 255, percentuale * i);
				colours[i] = brighter;
			}
		}
		else if (color_code == 2) { //codice per il giallo
			for (int i = 0; i < num_col; i++) {
				int brighter = Color.rgb(255, 255, percentuale * i);
				colours[i] = brighter;
			}
		}
		else if (color_code == 3) { //codice per il rosso
			for (int i = 0; i < num_col; i++) {
				int brighter = Color.rgb(255, percentuale * i, percentuale * i);
				colours[i] = brighter;
			}
		}
		else { //in caso di codice sbagliato mette tutto in bianco
			for (int i = 0; i < num_col; i++) {
				colours[i] = Color.WHITE;
			}
		}
		return colours;
	}*/

	public static int getColours(int level, int color_code) {
		int colorCode = 0;
		Color color = new Color();
		 //sfumature di verde
			if (level<=11)
				colorCode = color.parseColor(UNDER_SUPPLY_0_11);
			else if (level>=12 && level<=22)
				colorCode = color.parseColor(UNDER_SUPPLY_12_22);
			else if (level>=23 && level<=33)
				colorCode = color.parseColor(UNDER_SUPPLY_23_33);
		 //sfumature di giallo
			if (level>=34 && level<=45)
				colorCode = color.parseColor(NORMAL_SUPPLY_34_45);
			else if (level>=46 && level<=57)
				colorCode = color.parseColor(NORMAL_SUPPLY_46_57);
			else if (level>=58 && level<=67)
				colorCode = color.parseColor(NORMAL_SUPPLY_58_67);
		 //sfumature di rosso	
			if (level>=68 && level<=79)
				colorCode = color.parseColor(OVER_SUPPLY_68_79);
			else if (level>=80 && level<=91)
				colorCode = color.parseColor(OVER_SUPPLY_80_91);
			else if (level>=92)
				colorCode = color.parseColor(OVER_SUPPLY_92_100);
			
		return colorCode;
	}

}