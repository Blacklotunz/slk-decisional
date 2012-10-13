package com.slk.application;

import android.graphics.Color;


public class color_test {

	/**
	 * @param args
	 */
	
	/*
	 * 
	 * ISTRUZIONI
	 * chiamare la funzione getColours(x, y)
	 * con x: numero di colori che si vogliono ottenere
	 * y: codice del colore {1,2,3} 1 = verde, 2 = giallo, 3 = rosso
	 * 
	 */
	
	
	
//	public static void main(String[] args) {
//		//		Color[] colours = getColours(13, 1); //codice 1 per il verde
//		// 		Color[] colours = getColours(17, 2); //codice 2 per il giallo
//		int[] colours = getColours(10, 3); //codice 3 per il rosso
//		for (int i = 0; i<colours.length; i++) {
//			System.out.println("Colore : " + colours[i]);
//		}
//	}


	public static int[] getColours(int num_col, int color_code) {
		int[] colours = new int[num_col];
		int percentuale = 185/num_col; //185 per non prendere colori troppo chiari (troppo vicini a 255)
		//System.out.println(percentuale);
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
	}
}