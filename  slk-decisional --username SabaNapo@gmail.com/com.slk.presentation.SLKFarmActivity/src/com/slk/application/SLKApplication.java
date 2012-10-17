package com.slk.application;


import java.util.ArrayList;
import java.util.Calendar;
import com.slk.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.slk.bean.HistoryProdotto;
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

		if(c.moveToFirst()){  //se va alla prima entry, il cursore non � vuoto
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

		int idCol = c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_ID); //columns index
		int nameCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_NAME); 
		int varietyCol = c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_VARIETY);
		int priceCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_PRICE);
		int imgCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_IMG);
		int coloreCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_COLOR);
		int annoCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_ANNO);
		int meseCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_MESE);
		int qVendAnnPreCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC);
		int qPrevAnnCorrCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR);
		int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);

		HistoryProdotto prod;
		if(c.moveToFirst()){  //se va alla prima entry, il cursore non � vuoto
			do {
				//estrazione dei dati dalla entry del cursor
				prod=new HistoryProdotto(c.getString(idCol),c.getString(nameCol),c.getString(varietyCol),c.getDouble(priceCol),c.getString(imgCol),c.getInt(coloreCol),c.getInt(annoCol),c.getInt(meseCol),c.getInt(qVendAnnPreCol),c.getInt(qPrevAnnCorrCol),c.getInt(quantCol));
				toReturn.add(prod);         
			} while (c.moveToNext());//iteriamo al prossimo elemento
		}

		db.close();
		return toReturn;

	}

	//Controlla se nel database c'� un prodotto che � stato coltivato in anno
	public boolean isProductInHistorybyYear(String name,int anno){
		db.open();
		if(db.getHistoryProductbyYear(name, anno).getCount()==0){
			db.close();
			return false;}
		db.close();
		return true;

	}

	/*Metodo per inserire o aggiornare la quantit� di prodotto nell anno in corso
	 * Se inserisce usa il metodo
	 * insertProductInHistory(name, price, img, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, stagione, q_prodotta)
	 * quest� avverr� la prima volta che si inserisce il prodotto nella history
	 * Se invece fa l'update incrementa la quantit� di prodotto che l'utente ha previsto di produrre
	 * Controlla se il prodotto � presente nella history in base al nome del prodotto e all anno tramite il metodo isProductInHistory
	 * */

	/*public void insertOrUpdateProductInHistory(String name,double price,int img,int colore,int anno,int mese,int q_venduta_anno_prec,int q_prev_anno_corr,int stagione,int q_prodotta,int q_prev_utente){
		//Se il prodotto � presente gia nella tabella history nell anno preso in considerazione incrementa la quantit� preventivata nell'anno dall utente
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

	public void insertOrUpdateProductInHistory(String id, String name,String variety, double price, String imgURL,int colore,int anno,int mese,int q_venduta_anno_prec,int q_prev_anno_corr,int q_prev_utente){
		db.open();
		Cursor c=db.getHistoryProductbyYear(name, anno);
		//Se il prodotto � presente gia nella tabella history nell anno preso in considerazione incrementa la quantit� preventivata nell'anno dall utente
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			int q_prodotta=c.getInt(quantCol);
			q_prodotta=q_prodotta+q_prev_utente;
			db.updateProductInHistory(name, q_prodotta);
			db.updateProductColorInHistory(name, colore);
		}
		else{
			//Prodotto non presente nell'anno preso in considerazione si aggiungera' una riga riguardante il prodotto
			db.insertProductInHistory(id, name,variety, price, imgURL, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, q_prev_utente);
		}
		db.close();
	}
	//Metodo di inserimento di un prodotto nella tabella Products
	//Non ci sono controlli fare attenzione nell uso potrebbe dare errori dato che la chiave � il nome del prodotto
	//due prodotti con lo stesso nome creerebbero problemi
	public void insertProduct(String id, String name,String variety,double price,String img,int lista,int supp,int q_venduta_anno_prec,int q_prev_anno_corr){
		db.open();
		db.insertProduct(id,name, variety, price, img, lista, supp,q_venduta_anno_prec, q_prev_anno_corr);
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
		if(db.fetchProducts().getCount()==0){//inserimento dati, solo se il db � vuoto
			Log.i("SLKApplication", "DB vuoto");

			db.insertProduct("bananaid","banana","variety", 3,"url",1, 10 ,2000,1000);
			db.insertProduct("carrotid","carrot", "variety",		2,	 "url",		 1,10, 1000,	500);
			db.insertProduct("cabbageid", "cabbage", "variety",		1,	 "url",		 1, 10,2000,	1000);
			db.insertProduct("cucumberid","cucumber",	"variety",1.5, "url",	 1, 10,500,	100);
			db.insertProduct("onionid","onion", "variety",	0.5, "url",	 1, 10,700,	300);
			db.insertProduct("strawberryid","strawberry","variety", 	1.0, "url", 1, 10,8000,	0);
			db.insertProduct("cherryid","cherry", "variety",	2.0, "url", 1, 10,5500,	0);
			db.insertProduct("kiwiid","kiwi", "variety",		1.2, "url", 1,10, 1000,	0);
			db.insertProduct("saladid","salad", "variety",	1.0, "url", 2, 40,3500,	3300);
			db.insertProduct("zucchiniid","zucchini", "variety",	1.0, "url", 2, 40,200,	150);
			db.insertProduct("eggplantid","eggplant", "variety",	1.3, "url", 2, 40,450,	400);
			db.insertProduct("watermelonid","watermelon", "variety",	0.4, "url", 2, 40,3450,	3300);
			db.insertProduct("basilid","basil", "variety",	0.5, "url", 2, 40,1800,	1700);
			db.insertProduct("savoy cabbageid","savoy cabbage", "variety",		0.6, "url", 2,40, 2000,	1999);
			db.insertProduct("appleid","apple", 	"variety",	0.5, "url", 3,80, 400,	500);
			db.insertProduct("pearid","pear", 	"variety",	2.0, "url", 3, 80,1250,	1300);
			db.insertProduct("orangeid","orange", "variety",	1.2, "url", 3,80, 2450,	3000);
			db.insertProduct("mangoid","mango", 	"variety",	1.2, "url", 3, 80,6760,	7000);
			db.insertProduct("khakiid","khaki", "variety",	1.1, "url", 3,80, 1200,	1250);
			db.insertProduct("sweet pepperid","sweet pepper", "variety",	1.8, "url", 3, 80,4500,	4650);
		}
		db.close();
	}

	/*Inserisce i prodotti nel database per dettagli sulla firma del metodo insertHistoryProduct
	 * fare riferimento alla classe SLKStorage nel package com.slk.storage*/
	public void setHistoryProducts(){
		db.open();
		if(db.fetchHistoryProducts().getCount()==0){//inserimento dati, solo se il db � vuoto
			Log.i("SLKApplication", "DB HISTORY vuoto");

			db.insertProductInHistory("bananaid","banana","variety", 3.5,"url",1, 2010,10,200,500,1000);
			db.insertProductInHistory("carrotid","carrot", "variety",2.3,"url",2, 2010,03,200,600,500);
		}
		db.close();
	}
}