package com.slk.application;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.Log;

import com.slk.bean.HistoryProdotto;
import com.slk.bean.Product;
import com.slk.http.HttpConnector;
import com.slk.storage.SLKStorage;

public class Application {
	SLKStorage db;
	Context c;
	public static String local="en";

	public Application(Context ct){
		//istanzia il layer storage
		db=new SLKStorage(ct);
		c = ct;
	}

	//return current year
	public int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);	
	}
	//return current month
	public int getCurrentMonth(){
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	public boolean isThisProductCultivatedLastYear(String id){
		int lastYear=this.getCurrentYear()-1;
		return this.isProductInHistorybyYear(id, lastYear);

	}

	public String getLastYearQuantity(String id){
		int lastYear=this.getCurrentYear()-1;
		String toReturn;
		db.open();
		Cursor c=db.getHistoryProductbyYear(id, lastYear, HttpConnector.farmerId);
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			toReturn="Last production: "+c.getInt(quantCol)+" Kg";
		}
		else{
			toReturn="Not have produced this product last year";
		}
		c.close();
		db.close();
		return toReturn;
	}

	public int getCurrentQuantity(String id){
		int toReturn=0;
		db.open();
		Cursor c=db.getHistoryProductbyYear(id, this.getCurrentYear(), HttpConnector.farmerId);
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			toReturn=c.getInt(quantCol);
		}
		else{
			toReturn=0;
		}
		db.close();
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
		if(ProdType.equals("all")){
			c=db.fetchProducts();
		}
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
		int colCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_COLOR);
		int weightCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_WEIGHT);
		int sizeCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_SIZE);
		int cropIdCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_CROPID);
		int cultivIdCol=c.getColumnIndex(SLKStorage.ProductsMetaData.PRODUCT_CULTIVARID);
		Product prod;

		if(c.moveToFirst()){  //se va alla prima entry, il cursore non Ë vuoto
			do {
				//estrazione dei dati dalla entry del cursor
				prod=new Product(c.getString(cropIdCol),c.getString(cultivIdCol),c.getString(idCol),c.getString(nameCol),c.getString(varietyCol),c.getDouble(priceCol),c.getString(colCol),c.getString(weightCol),c.getString(sizeCol),c.getString(imgCol),c.getInt(productionLevelCol),c.getInt(listaCol),c.getDouble(qVendAnnPreCol),c.getDouble(qPrevAnnCorrCol));
				toReturn.add(prod);
			} while (c.moveToNext());//iteriamo al prossimo elemento
		}


		if (!ProdType.equals("all")){
			if (toReturn.size()!=0){
				//set color for each product
				for(Product p : toReturn){
					p.setColor(ColorSetter.getColours(p.getProductionLevel(),p.getLista()));
				}
			}
		}
		c.close();
		db.close();
		return toReturn;
	}

	//metodo richiamato dagli altri metodi getProducts...in base al valore passato cambia la query e la fa eseguire allo storage layer
	public ArrayList<HistoryProdotto> getHistoryProducts(){
		db.open();
		ArrayList<HistoryProdotto> toReturn=new ArrayList<HistoryProdotto>();
		Cursor c=db.fetchHistoryProducts(HttpConnector.farmerId);

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
		if(c.moveToFirst()){  //se va alla prima entry, il cursore non Ë vuoto
			do {
				//estrazione dei dati dalla entry del cursor
				prod=new HistoryProdotto(c.getString(idCol),c.getString(nameCol),c.getString(varietyCol),c.getDouble(priceCol),c.getString(imgCol),c.getInt(coloreCol),c.getInt(annoCol),c.getInt(meseCol),c.getDouble(qVendAnnPreCol),c.getDouble(qPrevAnnCorrCol),c.getDouble(quantCol));
				toReturn.add(prod);         
			} while (c.moveToNext());//iteriamo al prossimo elemento
		}
		c.close();
		db.close();
		return toReturn;

	}

	//Controlla se nel database c'Ë un prodotto che Ë stato coltivato in anno
	public boolean isProductInHistorybyYear(String name,int anno){
		db.open();
		if(db.getHistoryProductbyYear(name, anno, HttpConnector.farmerId).getCount()==0){
			db.close();
			return false;}
		db.close();
		return true;

	}

	/*Metodo per inserire o aggiornare la quantit‡ di prodotto nell anno in corso
	 * Se inserisce usa il metodo
	 * insertProductInHistory(name, price, img, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, stagione, q_prodotta)
	 * questÚ avverr‡ la prima volta che si inserisce il prodotto nella history
	 * Se invece fa l'update incrementa la quantit‡ di prodotto che l'utente ha previsto di produrre
	 * Controlla se il prodotto Ë presente nella history in base al nome del prodotto e all anno tramite il metodo isProductInHistory
	 * */
	public void insertOrUpdateProductInHistory(String id, String name,String variety, double price, String imgURL,int colore,int anno,int mese,double q_venduta_anno_prec,double q_prev_anno_corr,double q_prev_utente){
		db.open();

		Cursor c=db.getHistoryProductbyYear(id, anno, HttpConnector.farmerId);
		//Se il prodotto Ë presente gia nella tabella history nell anno preso in considerazione incrementa la quantit‡ preventivata nell'anno dall utente
		if(c.moveToFirst()){
			int quantCol=c.getColumnIndex(SLKStorage.HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA);
			double q_prodotta=c.getDouble(quantCol);
			q_prodotta=q_prodotta+q_prev_utente;
			db.updateProductInHistory(id, q_prodotta,HttpConnector.farmerId);
			db.updateProductColorInHistory(id, colore, HttpConnector.farmerId);
		}
		else{
			//Prodotto non presente nell'anno preso in considerazione si aggiungera' una riga riguardante il prodotto
			db.insertProductInHistory(id, name,variety, price, imgURL, colore, anno, mese, q_venduta_anno_prec, q_prev_anno_corr, q_prev_utente, HttpConnector.farmerId);
		}
		c.close();
		db.close();
	}
	//Metodo di inserimento di un prodotto nella tabella Products
	public void insertProduct(String id, String name,String variety,double price,String color, String weight, String size,String img,int lista,int supp,int q_venduta_anno_prec,int q_prev_anno_corr, String cropId, String cultivarId){
		db.open();
		db.insertProduct(id,name, variety, price,color,weight,size,img, lista, supp,q_venduta_anno_prec, q_prev_anno_corr, cropId, cultivarId);
		db.close();		
	}

	//Incrementa sul database la quantita' preventivata del prodotto dopo la scelta dell'utente
	/*public void updateProduct(String id,int productionLevel,Double q_prev_anno_corr,Double q_prev_utente){
		q_prev_anno_corr=q_prev_anno_corr+q_prev_utente;
		db.open();
		db.updateProduct(id, productionLevel, q_prev_anno_corr);
		db.close();
	}*/

	public void updateListProduct(String id,int lista){
		db.open();
		db.updateListaProduct(id, lista);
		db.close();
	}


	/*Inserisce i prodotti nel database per dettagli sulla firma del metodo insertProduct
	 * fare riferimento alla classe SLKStorage nel package com.slk.storage*/
	public void setProducts(){
		db.open();

		if(db.fetchProducts().getCount()==0){//inserimento dati, solo se il db Ë vuoto
			Log.i("SLKApplication", "DB vuoto");
			/*			db.insertProduct("bananaid","banana","variety", 3.6,"url",1,11,2000,0);
			db.insertProduct("carrotid","carrot", "variety",2.1,"url",1,1, 1000,0);
			db.insertProduct("cabbageid", "cabbage", "variety",1.3,"url",1, 12,2000,0);
			db.insertProduct("cucumberid","cucumber",	"variety",1.5, "url",1,16,500,0);
			db.insertProduct("onionid","onion", "variety",0.5,"url",1,2,700,0);
			db.insertProduct("strawberryid","strawberry","variety",1.0, "url", 1, 33,8000,	0);
			db.insertProduct("cherryid","cherry", "variety",	2.0, "url", 2, 34,5500,	0);
			db.insertProduct("kiwiid","kiwi", "variety",		1.2, "url", 2,50, 1000,	0);
			db.insertProduct("saladid","salad", "variety",	1.0, "url", 2, 67,3500,	3300);
			db.insertProduct("zucchiniid","zucchini", "variety",	1.0, "url", 3, 79,200,	0);
			db.insertProduct("watermelonid","watermelon", "variety",	0.4, "url", 3, 80,3450,	0);
			db.insertProduct("khakiid","khaki", "variety",	1.1, "url", 3,92, 1200,0);
			 */
			db.close();
		}
	}

	/*Inserisce i prodotti nel database per dettagli sulla firma del metodo insertHistoryProduct
	 * fare riferimento alla classe SLKStorage nel package com.slk.storage*/
	public void setHistoryProducts(){
		db.open();
		if(db.fetchHistoryProducts(HttpConnector.farmerId).getCount()==0){//inserimento dati, solo se il db Ë vuoto
			Log.i("SLKApplication", "DB HISTORY vuoto");

			db.insertProductInHistory("bananaid","banana","variety", 3.5,"url",1, 2010,10,200,500,1000,HttpConnector.farmerId);
			db.insertProductInHistory("carrotid","carrot", "variety",2.3,"url",2, 2010,03,200,600,500,HttpConnector.farmerId);
		}
		db.close();
	}


	/*
	 * download the products from WS and set into DB, after insert download image from url and save in SD with name == id of product
	 */
	public void setProductsFromWS(boolean update) {
		db.open();
		HttpConnector http = new HttpConnector();
		ArrayList<JSONObject> products = new ArrayList<JSONObject>();
		try {
			products = http.fetchProducts();
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if(db.fetchProducts().getCount()==0 || update){//inserimento dati, solo se il db Ë vuoto
			for(JSONObject obj : products){
				Log.i("names",""+obj.names());
				try {
					JSONObject objj = obj;
					JSONObject pobjj=obj.getJSONObject("production");
					JSONObject cobjj=obj.getJSONObject("characteristics");

					/*avoid error on parse*/
					Double currentProduction;
					if(pobjj.getString("currentProduction").equalsIgnoreCase("null"))
						currentProduction = 0.0;
					else
						currentProduction = Double.parseDouble(pobjj.getString("currentProduction"));
					/* */
					if(update)
						db.updateProduct(""+objj.getString("cropName")+objj.getString("cultivarName"), objj.getString("cropName"), objj.getString("cultivarName"),0.0,cobjj.getString("color"),cobjj.getString("weight"),cobjj.getString("size"),objj.getString("images"), Application.getListOfProduct((objj.getString("myType"))), Double.parseDouble(pobjj.getString("percentageOfProduction")), Double.parseDouble(pobjj.getString("maxProduction")), currentProduction, objj.getString("cropId"),objj.getString("cultivarId"));
					else
						db.insertProduct(""+objj.getString("cropName")+objj.getString("cultivarName"), objj.getString("cropName"), objj.getString("cultivarName"),0.0,cobjj.getString("color"),cobjj.getString("weight"),cobjj.getString("size"),objj.getString("images"), Application.getListOfProduct((objj.getString("myType"))), Double.parseDouble(pobjj.getString("percentageOfProduction")), Double.parseDouble(pobjj.getString("maxProduction")), currentProduction, objj.getString("cropId"),objj.getString("cultivarId"));

					//download image
					ImageHandler.downloadImageFromUrl(objj.getString("images"), ""+objj.getString("cropName")+objj.getString("cultivarName"), c);			
				} catch (NumberFormatException e) {					
					e.printStackTrace();
				} catch (JSONException e) {				
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
		db.close();
	}

	private static int getListOfProduct(String type){
		if(type.equalsIgnoreCase("vegetable")){
			return 1;
		}else if(type.equalsIgnoreCase("fruits")){
			return 2;
		}else
			return 3;
	}


	/*
	 * support method for give a list of production for a product based on supply level quantity
	 * 
	 * NOT USED
	 */
	private static int getListOfProduct(int productionLevel){
		if (productionLevel<=33)
			return 1;
		else if (productionLevel>=34 && productionLevel<=67)
			return 2;
		else if (productionLevel>=68)
			return 3;
		else 
			return 0;
	}



	public Product insertProductInWS(Double quantity, String cropid, String cultivarid){
		HttpConnector http = new HttpConnector();
		Map<String,String> map = http.insertProduction(quantity, cropid, cultivarid);
		String production_id = map.get("production_id");
		String production_quantity = map.get("production_quantity");
		String farm_id = map.get("farm_id");
		Product toReturn = null;
		//db.insertProduction(production_id, production_quantity, farm_id);
		setProductsFromWS(true);
		ArrayList<Product> products = getAllProducts();

		for(Product p : products){

			if((p.getCropId().equalsIgnoreCase(cropid)) && (p.getCultivarId().equalsIgnoreCase(cultivarid))){
				Log.i("trovato!!!", p.getId());
				toReturn = p;
				return toReturn;
			}
		}
		
		Log.e("error", "missing product");
		return toReturn;
	}

	//set the language of the app
	public static void setLanguage(Context context)
	{
		Resources res = context.getResources();
	    DisplayMetrics dm = res.getDisplayMetrics();
	    android.content.res.Configuration conf = res.getConfiguration();
		if (local.equalsIgnoreCase("en"))
			conf.locale = Locale.getDefault();
		else 
			conf.locale = new Locale("si");
		res.updateConfiguration(conf, dm);
	}

	public void deleteProductTable() {
		db.open();
		db.dropProductTable();
		db.close();
	}

}
