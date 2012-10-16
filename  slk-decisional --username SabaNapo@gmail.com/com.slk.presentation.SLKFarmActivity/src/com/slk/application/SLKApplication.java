package com.slk.application;


import java.util.ArrayList;
import java.util.Calendar;
import com.slk.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.slk.bean.Product;
import com.slk.storage.SLKStorage;

public class SLKApplication {
	SLKStorage db;

	public SLKApplication(Context ct){
		//istanzia il layer storage
		db=new SLKStorage(ct);	 
	}

	//Ritorna l'anno corrente
	public int getCurrentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);	
	}

	public boolean isThisProductCultivatedLastYear(String name){
		int lastYear=this.getCurrentYear()-1;
		return this.isProductInHistorybyYear(name, lastYear);

	}

	public String getLastYearQuantity(String name){
		int lastYear=this.getCurrentYear()-1;
		String toReturn;
		db.open();
		Cursor c=db.getHistoryProductbyYear(name, lastYear);
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			toReturn="Last production: "+c.getInt(quantCol)+" Kg";
		}
		else{
			toReturn="Not have produced this product last year";
		}
		return toReturn;
	}

	public int getCurrentQuantity(String name){
		int toReturn=0;
		db.open();
		Cursor c=db.getHistoryProductbyYear(name, this.getCurrentYear());
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			toReturn=c.getInt(quantCol);
		}
		else{
			toReturn=0;
		}
		return toReturn;
	}


	//ritorna un arraylist che contiene tutti i prodotti del database mappati ognuno in una classe prodotto
	public ArrayList<Product> getAllProducts(){
		return getSelectProducts("all");
	}
	/*
	 * IMPORTANTE: 	IL CAMPO COLORE DELL OGGETTO PRODOTTO CHE VERRA MEMORIZZATO NELL ARRAYLIST 
	 * HA IMPOSTATO IL CAMPO COLORE (IL CONTORNO DELLA LISTA) A 00000 PERCHE' A QUESTO PUNTO 
	 * DELL ESECUZIONE NON SI SA LA GRADAZIONE DI COLORE CHE AVRA'
	 * SI DEVE SETTARE SUCCESSIVAMENTE ATTRAVERSO IL METODO setColore() 
	 * */
	//ritorna un arraylist che contiene tutti i prodotti della lista verde mappati ognuno in una classe prodotto
	public ArrayList<Product> getGreenProducts(){
		return getSelectProducts("green");	
	}
	//ritorna un arraylist che contiene tutti i prodotti della lista gialla mappati ognuno in una classe prodotto
	public ArrayList<Product> getYellowProducts(){
		return getSelectProducts("yellow");	
	}
	//ritorna un arraylist che contiene tutti i prodotti della lista rossa mappati ognuno in una classe prodotto
	public ArrayList<Product> getRedProducts(){
		return getSelectProducts("red");	
	}

	//metodo richiamato dagli altri metodi getProducts...in base al valore passato cambia la query e la fa eseguire allo storage layer
	public ArrayList<Product> getSelectProducts(String ProdType){
		db.open();
		ArrayList<Product> toReturn=new ArrayList<Product>();
		Cursor c=null;
		if(ProdType.equals("all"))
			c=db.fetchProducts();
		if(ProdType.equals("green"))
			c=db.fetchGreenProducts();
		if(ProdType.equals("yellow"))
			c=db.fetchYellowProducts();
		if(ProdType.equals("red"))
			c=db.fetchRedProducts();
		
		//indici delle colonne
		int idCol =c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_ID);
		int nameCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_NAME);  
		int priceCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_PRICE);
		int imgCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_IMG);
		int listaCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_LISTA);
		int qVendAnnPreCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC);
		int qPrevAnnCorrCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR);
		int productionLevelCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_PRODUCTION_LEVEL);
		int varietyCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_VARIETY);
		Product prod;
		
		if(c.moveToFirst()){  //se va alla prima entry, il cursore non è vuoto
			do {
				//estrazione dei dati dalla entry del cursor
				prod=new Product(c.getString(idCol),c.getString(nameCol), c.getString(varietyCol),c.getDouble(priceCol),c.getString(imgCol),c.getInt(productionLevelCol),c.getInt(listaCol),c.getInt(qVendAnnPreCol),c.getInt(qPrevAnnCorrCol));
				toReturn.add(prod);
				
				//String s="Product Name:"+c.getString(nameCol)+", Price:"+c.getDouble(priceCol)+", Imm:"+c.getInt(imgCol)+", Lista:"+c.getInt(listaCol)+", Quantita venduta anno prec:"+c.getInt(qVendAnnPreCol)+", Quantita preventivata:"+c.getInt(qPrevAnnCorrCol)+", Stagione:"+c.getInt(stagioneCol); 
				//Log.v("product fetched", s);   
				
			} while (c.moveToNext());//iteriamo al prossimo elemento
		}

		db.close();
		if (!ProdType.equals("all")){
			if (toReturn.size()!=0){
				int[] colors=ColorSetter.getColours(toReturn.size(),toReturn.get(0).getLista());
				for(int i=0;i<toReturn.size();i++){
					Product p=toReturn.get(i);
					p.setColore(colors[toReturn.size()-(i+1)]);
					Log.v("colore",""+ colors[i]);
				}
			}
		}
		return toReturn;
	}

	//metodo richiamato dagli altri metodi getProducts...in base al valore passato cambia la query e la fa eseguire allo storage layer
	public ArrayList<HistoryProdotto> getHistoryProducts(){
		db.open();
		ArrayList<HistoryProdotto> toReturn=new ArrayList<HistoryProdotto>();
		Cursor c=db.fetchHistoryProducts();
		int nameCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_NAME);  //indici delle colonne
		int priceCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_PRICE);
		int imgCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_IMG);
		int coloreCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_COLOR);
		int annoCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_ANNO);
		int meseCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_MESE);
		int qVendAnnPreCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC);
		int qPrevAnnCorrCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR);
		int stagioneCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_STAGIONE);
		int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
		HistoryProdotto prod;
		if(c.moveToFirst()){  //se va alla prima entry, il cursore non è vuoto
			do {
				//estrazione dei dati dalla entry del cursor
				prod=new HistoryProdotto(c.getString(nameCol),c.getDouble(priceCol),c.getInt(imgCol),c.getInt(coloreCol),c.getInt(annoCol),c.getInt(meseCol),c.getInt(qVendAnnPreCol),c.getInt(qPrevAnnCorrCol),c.getInt(stagioneCol),c.getInt(quantCol));
				toReturn.add(prod);
				//s="Product Name:"+c.getString(nameCol)+", Price:"+c.getDouble(priceCol)+", Imm:"+c.getInt(imgCol)+", Lista:"+c.getInt(listaCol)+", Quantita venduta anno prec:"+c.getInt(qVendAnnPreCol)+", Quantita preventivata:"+c.getInt(qPrevAnnCorrCol)+", Stagione:"+c.getInt(stagioneCol); 
				//Log.v("ciao", s);                
			} while (c.moveToNext());//iteriamo al prossimo elemento
		}

		db.close();
		return toReturn;

	}

	//Controlla se nel database c'è un prodotto che è stato coltivato in anno
	public boolean isProductInHistorybyYear(String name,int anno){
		db.open();
		if(db.getHistoryProductbyYear(name, anno).getCount()==0){
			db.close();
			return false;}
		db.close();
		return true;

	}

	/*Metodo per inserire o aggiornare la quantità di prodotto nell anno in corso
	 * Se inserisce usa il metodo
	 * insertProductInHistory(name, price, img, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, stagione, q_prodotta)
	 * questò avverrà la prima volta che si inserisce il prodotto nella history
	 * Se invece fa l'update incrementa la quantità di prodotto che l'utente ha previsto di produrre
	 * Controlla se il prodotto è presente nella history in base al nome del prodotto e all anno tramite il metodo isProductInHistory
	 * */

	/*public void insertOrUpdateProductInHistory(String name,double price,int img,int colore,int anno,int mese,int q_venduta_anno_prec,int q_prev_anno_corr,int stagione,int q_prodotta,int q_prev_utente){
		//Se il prodotto è presente gia nella tabella history nell anno preso in considerazione incrementa la quantità preventivata nell'anno dall utente
		if(this.isProductInHistorybyYear(name, anno)){
			db.open();
			q_prodotta=q_prodotta+q_prev_utente;
			db.updateProductInHistory(name, q_prodotta);
		}
		else{
			db.open();
			//Prodotto non presente nell'anno preso in considerazione si aggiungera' una riga riguardante il prodotto
			db.insertProductInHistory(name, price, img, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, stagione, q_prodotta);
		}
		db.close();
	}*/
	
	public void insertOrUpdateProductInHistory(String name,double price,String imgURL,int colore,int anno,int mese,int q_venduta_anno_prec,int q_prev_anno_corr,int q_prev_utente){
		db.open();
		Cursor c=db.getHistoryProductbyYear(name, anno);
		//Se il prodotto è presente gia nella tabella history nell anno preso in considerazione incrementa la quantità preventivata nell'anno dall utente
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			int q_prodotta=c.getInt(quantCol);
			q_prodotta=q_prodotta+q_prev_utente;
			db.updateProductInHistory(name, q_prodotta);
			db.updateProductColorInHistory(name, colore);
		}
		else{
			//Prodotto non presente nell'anno preso in considerazione si aggiungera' una riga riguardante il prodotto
			db.insertProductInHistory(name, price, imgURL, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, q_prev_utente);
		}
		db.close();
	}
	//Metodo di inserimento di un prodotto nella tabella Products
	//Non ci sono controlli fare attenzione nell uso potrebbe dare errori dato che la chiave è il nome del prodotto
	//due prodotti con lo stesso nome creerebbero problemi
	public void insertProduct(String name,double price,int img,int lista,int q_venduta_anno_prec,int q_prev_anno_corr,int stagione){
		db.open();
		db.insertProduct(name, price, img, lista, q_venduta_anno_prec, q_prev_anno_corr, stagione);
		db.close();		
	}

	//Incrementa sul database la quantita' preventivata del prodotto dopo la scelta dell'utente
	public void updateProduct(String name,int q_prev_anno_corr,int q_prev_utente){
		q_prev_anno_corr=q_prev_anno_corr+q_prev_utente;
		db.open();
		db.updateProduct(name, q_prev_anno_corr);
		db.close();
	}
	
	public void updateListProduct(String name,int lista){
		db.open();
		db.updateListaProduct(name, lista);
		db.close();
	}


	/*Inserisce i prodotti nel database per dettagli sulla firma del metodo insertProduct
	 * fare riferimento alla classe SLKStorage nel package com.slk.storage*/
	public void setProducts(){
		db.open();
		if(db.fetchProducts().getCount()==0){//inserimento dati, solo se il db è vuoto
			Log.i("SLKApplication", "DB vuoto");
			db.insertProduct("banana", 		3,	 R.drawable.banana,		 1, 2000,	1000,	1);
			db.insertProduct("carrot", 		2,	 R.drawable.carota,		 1, 1000,	500,	2);
			db.insertProduct("cabbage", 		1,	 R.drawable.cavolo,		 1, 2000,	1000,	3);
			db.insertProduct("cucumber",	1.5, R.drawable.cetriolo,	 1, 500,	100,	4);
			db.insertProduct("onion", 	0.5, R.drawable.cipolla,	 1, 700,	300,	1);
			db.insertProduct("strawberry", 	1.0, R.drawable.fragole, 1, 8000,	0,		1);
			db.insertProduct("cherry", 	2.0, R.drawable.ciliegie, 1, 5500,	0,		3);
			db.insertProduct("kiwi", 		1.2, R.drawable.kiwi, 1, 1000,	0,		3);
			db.insertProduct("salad", 	1.0, R.drawable.insalata, 2, 3500,	3300,		2);
			db.insertProduct("zucchini", 	1.0, R.drawable.zucchine, 2, 200,	150,		4);
			db.insertProduct("eggplant", 	1.3, R.drawable.melanzana, 2, 450,	400,		4);
			db.insertProduct("watermelon", 	0.4, R.drawable.cocomero, 2, 3450,	3300,		4);
			db.insertProduct("basil", 	0.5, R.drawable.basilico, 2, 1800,	1700,		4);
			db.insertProduct("savoy cabbage", 		0.6, R.drawable.verza, 2, 2000,	1999,		1);
			db.insertProduct("apple", 		0.5, R.drawable.mela, 3, 400,	500,		2);
			db.insertProduct("pear", 		2.0, R.drawable.pera, 3, 1250,	1300,		1);
			db.insertProduct("orange", 	1.2, R.drawable.arancia, 3, 2450,	3000,		3);
			db.insertProduct("mango", 		1.2, R.drawable.mango, 3, 6760,	7000,		1);
			db.insertProduct("khaki", 	1.1, R.drawable.chachi, 3, 1200,	1250,		1);
			db.insertProduct("sweet pepper", 	1.8, R.drawable.peperoni, 3, 4500,	4650,		2);
		}
		db.close();
	}

	/*Inserisce i prodotti nel database per dettagli sulla firma del metodo insertHistoryProduct
	 * fare riferimento alla classe SLKStorage nel package com.slk.storage*/
	public void setHistoryProducts(){
		db.open();
		if(db.fetchHistoryProducts().getCount()==0){//inserimento dati, solo se il db è vuoto
			Log.i("SLKApplication", "DB vuoto");
			
			db.insertProductInHistory("banana", 3,"url",Color.RED,2011,3,2000,2000,150);
			db.insertProductInHistory("carrot", 2,"url",Color.GREEN,2011,7,170,170,200);
			db.insertProductInHistory("savoy cabbage", 1,"url",Color.YELLOW,2010,10,1100,1100,300);
			db.insertProductInHistory("cucumber", 1.5,"url",Color.YELLOW,2010,12,120,120,50);
		}
		db.close();
	}
}
