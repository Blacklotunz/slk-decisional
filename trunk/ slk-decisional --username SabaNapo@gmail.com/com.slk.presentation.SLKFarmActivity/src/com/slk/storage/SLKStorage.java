package com.slk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SLKStorage {  

	SQLiteDatabase mDb;
	DbHelper mDbHelper;
	Context mContext;
	private static final String DB_NAME="SLKFarmDB";//nome del db
	private static final int DB_VERSION=1; //numero di versione del nostro db

	public SLKStorage(Context ctx){
		mContext=ctx;
		//Ad ogni esecuzione cancella il database, utile in fase di debug
		//mContext.deleteDatabase(DB_NAME);
		mDbHelper=new DbHelper(ctx, DB_NAME, null, DB_VERSION);   //quando istanziamo questa classe, istanziamo anche l'helper (vedi sotto) 
	}

	public void clear(){
		mContext.deleteDatabase(DB_NAME);
	}

	//open daabase. this metod will create it if does not exist
	public void open(){
		mDb=mDbHelper.getWritableDatabase();
		}

	//close the database connection
	public void close(){ 
		mDb.close();
		mDbHelper.close();
	}

	public void insertProduction(String production_id, String production_quantity, String farm_id){
		ContentValues cv = new ContentValues();
		cv.put(ProductionMetaData.PRODUCTION_ID, production_id);
		cv.put(ProductionMetaData.PRODUCTION_QUANTITY, production_quantity);
		cv.put(ProductionMetaData.FARM_ID, farm_id);
		mDb.insert(ProductionMetaData.PRODUCTION_TABLE, null, cv);
	}
	
	public void getProduction(){
		//To Do
	}
	
	/* Con questo metodi si inseriscono i prodotti nel database
	 * name prende in input il nome del prodotto che è anche la chiave
	 * price il prezzo del prodotto
	 * img è il codice che corrisponde al file immagine del prodotto codificato da R
	 * lista è usata per distinguere i prodotti tra: verdi lista=1, gialli lista=2, rossi lista=3
	 * q_venduta_anno_prec e' la quantita di prodotto venduta l'anno precedente
	 * q_prev_anno_corr e' la quantita preventivata fino ad ora
	 * */
	public void insertProduct(String id, String name, String variety,double price,String color, String weight,String size,String img,int lista,double supp,double q_venduta_anno_prec,double q_prev_anno_corr,String cropId,String cultivarId){ //metodo per inserire i dati
		ContentValues cv=new ContentValues();
		cv.put(ProductsMetaData.PRODUCT_ID, id);
		cv.put(ProductsMetaData.PRODUCT_NAME, name);
		cv.put(ProductsMetaData.PRODUCT_VARIETY, variety);
		cv.put(ProductsMetaData.PRODUCT_PRICE, price);
		cv.put(ProductsMetaData.PRODUCT_COLOR, color);
		cv.put(ProductsMetaData.PRODUCT_WEIGHT, weight);
		cv.put(ProductsMetaData.PRODUCT_SIZE, size);
		cv.put(ProductsMetaData.PRODUCT_IMG, img);
		cv.put(ProductsMetaData.PRODUCT_LISTA, lista);
		cv.put(ProductsMetaData.PRODUCT_PRODUCTION_LEVEL, supp);
		cv.put(ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC, q_venduta_anno_prec);
		cv.put(ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		cv.put(ProductsMetaData.PRODUCT_CROPID, cropId);
		cv.put(ProductsMetaData.PRODUCT_CULTIVARID, cultivarId);
		mDb.insert(ProductsMetaData.PRODUCTS_TABLE, null, cv);
	}
	/*Metodo per incrementare la quantità di prodotto dopo che è stata modificata nella schermata finale*/
	public void updateProduct(String id, String name, String variety,double price,String color, String weight,String size,String img,int lista,double supp,double q_venduta_anno_prec,double q_prev_anno_corr,String cropId,String cultivarId){
		ContentValues cv=new ContentValues();
		cv.put(ProductsMetaData.PRODUCT_ID, id);
		cv.put(ProductsMetaData.PRODUCT_NAME, name);
		cv.put(ProductsMetaData.PRODUCT_VARIETY, variety);
		cv.put(ProductsMetaData.PRODUCT_PRICE, price);
		cv.put(ProductsMetaData.PRODUCT_COLOR, color);
		cv.put(ProductsMetaData.PRODUCT_WEIGHT, weight);
		cv.put(ProductsMetaData.PRODUCT_SIZE, size);
		cv.put(ProductsMetaData.PRODUCT_IMG, img);
		cv.put(ProductsMetaData.PRODUCT_LISTA, lista);
		cv.put(ProductsMetaData.PRODUCT_PRODUCTION_LEVEL, supp);
		cv.put(ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC, q_venduta_anno_prec);
		cv.put(ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		cv.put(ProductsMetaData.PRODUCT_CROPID, cropId);
		cv.put(ProductsMetaData.PRODUCT_CULTIVARID, cultivarId);
		mDb.update(ProductsMetaData.PRODUCTS_TABLE, cv, ProductsMetaData.PRODUCT_ID+" = ?", new String[]{id});	
	}

	public void updateListaProduct(String id, int lista) {
		String strFilter = "id='" + id +"'";
		ContentValues args = new ContentValues();
		args.put(ProductsMetaData.PRODUCT_LISTA, lista);
		mDb.update("Crops", args, strFilter, null);	

	}

	/* Con questo metodi si inseriscono i prodotti del history nel database
	 * name prende in input il nome del prodotto che è anche la chiave
	 * price il prezzo del prodotto dell anno in considerazione
	 * img è il codice che corrisponde al file immagine del prodotto codificato da R
	 * colore è il colore al momento del savataggio
	 * anno è l'anno preso in considerazione
	 * mese è il mese preso in considerazione
	 * q_venduta_anno_prec e' la quantita di prodotto venduta l'anno precedente
	 * q_prev_anno_corr e' la quantita preventivata fino ad ora
	 * stagione distingue la stagione in cui si coltiva il prodotto: 1=primavera, 2=estate, 3=autunno, 4=inverno
	 * q_prodotta e' la quantita' che è stata prodotta dall utente nell anno preso in considerazione
	 * */
	public void insertProductInHistory(String id, String name, String variety, double price, String imgURL, int colore, int anno,int mese,double q_venduta_anno_prec,double q_prev_anno_corr, double q_prodotta){
		ContentValues cv=new ContentValues();
		cv.put(HistoryMetaData.PRODUCT_HISTORY_ID, id);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_NAME, name);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_VARIETY, variety);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_PRICE, price);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_IMG, imgURL);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_COLOR, colore);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_ANNO, anno);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_MESE, mese);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC, q_venduta_anno_prec);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA, q_prodotta);
		mDb.insert(HistoryMetaData.HISTORY_TABLE, null, cv);
	}

	public void updateProductInHistory(String id,double q_prodotta){
		String strFilter = "id='" + id+"'";
		ContentValues args = new ContentValues();
		args.put(HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA, q_prodotta);
		mDb.update("history", args, strFilter, null);	
	}
	public void updateProductColorInHistory(String id,int colore){
		String strFilter = "id='" + id+"'";
		ContentValues args = new ContentValues();
		args.put(HistoryMetaData.PRODUCT_HISTORY_COLOR, colore);
		mDb.update("history", args, strFilter, null);	
	}

	/*Metodo che ritorna il prodotto passato da name in un determinato anno(se è presente)*/
	public Cursor getHistoryProductbyYear(String id,int anno){ 
		return mDb.query(HistoryMetaData.HISTORY_TABLE, null,HistoryMetaData.PRODUCT_HISTORY_ID+"='" + id + "'"+" and "+HistoryMetaData.PRODUCT_HISTORY_ANNO+"='" + anno + "'",null,null,null,null);
		//return mDb.query("history", null,"id='" + id + "' and anno ='" + anno + "'",null,null,null,null);          
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla tabella history
	 *ordinati in base all anno*/	
	public Cursor fetchHistoryProducts(){ 
		return mDb.query(HistoryMetaData.HISTORY_TABLE, null,null,null,null,null,"anno",null);               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *contenuti nella tabella products*/
	public Cursor fetchProducts(){
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,null,null,null,null,ProductsMetaData.PRODUCT_PRODUCTION_LEVEL+" ASC",null);               
	}
	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria sottoproduzione(verdi)
	 *ordinati in base al prezzo*/	
	public Cursor fetchGreenProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==1",null,null,null,ProductsMetaData.PRODUCT_PRODUCTION_LEVEL+" ASC",null);               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria mediaproduzione(gialli)
	 *ordinati in base al prezzo*/	
	public Cursor fetchYellowProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==2",null,null,null,ProductsMetaData.PRODUCT_PRODUCTION_LEVEL+" ASC",null);               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria sovrapproduzione(rossi)
	 *ordinati in base al prezzo*/	
	public Cursor fetchRedProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==3",null,null,null,ProductsMetaData.PRODUCT_PRODUCTION_LEVEL+" ASC",null);               
	}

	//metadati della tabella production
	static public class ProductionMetaData {
		static public final String PRODUCTION_TABLE = "Production";
		static public final String PRODUCTION_ID = "productionid";
		static public final String PRODUCTION_QUANTITY = "quantity";
		static public final String FARM_ID = "farmid";
	}
	
	// i metadati della tabella products, accessibili ovunque
	static public class ProductsMetaData {  
		static public final String PRODUCTS_TABLE = "Crops";
		static public final String PRODUCT_ID = "id";
		static public final String PRODUCT_NAME = "name";
		static public final String PRODUCT_VARIETY = "variety";
		static public final String PRODUCT_PRICE = "price";
		static public final String PRODUCT_COLOR ="color";
		static public final String PRODUCT_WEIGHT ="weight";
		static public final String PRODUCT_SIZE ="size";
		static public final String PRODUCT_IMG = "immagine";
		static public final String PRODUCT_LISTA = "app_lista";
		static public final String PRODUCT_QUANT_VEND_ANNO_PREC = "q_vend_anno_prec";
		static public final String PRODUCT_QUANT_PREV_ANNO_CORR = "q_prev_anno_corr";
		static public final String PRODUCT_PRODUCTION_LEVEL = "productionLevel";
		static public final String PRODUCT_CROPID = "cropId";
		static public final String PRODUCT_CULTIVARID = "cultivarId";
	}

	// i metadati della tabella HISTORY, accessibili ovunque
	static public class HistoryMetaData {  
		static final String ID = "_id";
		static public final String HISTORY_TABLE = "history";
		static public final String PRODUCT_HISTORY_ID = "id";
		static public final String PRODUCT_HISTORY_NAME = "name";
		static public final String PRODUCT_HISTORY_VARIETY = "variety";
		static public final String PRODUCT_HISTORY_PRICE = "price";
		static public final String PRODUCT_HISTORY_IMG = "immagine";
		static public final String PRODUCT_HISTORY_COLOR = "colore";
		static public final String PRODUCT_HISTORY_ANNO = "anno";
		static public final String PRODUCT_HISTORY_MESE = "mese";
		static public final String PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC = "q_vend_anno_prec";
		static public final String PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR = "q_prev_anno_corr";
		static public final String PRODUCT_HISTORY_QUANTIT_PRODOTTA =	"q_prodotta";
		public static final String PRODUCT_VARIETY = "variety";
	}

	//sql code for the creation of PRODUCTION table
	private static final String PRODUCTION_TABLE_CREATE = "CREATE TABLE "
			+ ProductionMetaData.PRODUCTION_TABLE + " ("
			+ ProductionMetaData.PRODUCTION_ID + " text primary key, "
			+ ProductionMetaData.PRODUCTION_QUANTITY + " text, "
			+ ProductionMetaData.FARM_ID + " text);";
	
	//sql code for the creation of PRODUCTS table
	private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE "  
			+ ProductsMetaData.PRODUCTS_TABLE + " (" 
			+ ProductsMetaData.PRODUCT_ID + " text primary key, "
			+ ProductsMetaData.PRODUCT_NAME + " text not null, "
			+ ProductsMetaData.PRODUCT_VARIETY + " varchar[12], "
			+ ProductsMetaData.PRODUCT_PRICE + " double,"
			+ ProductsMetaData.PRODUCT_COLOR + " text,"
			+ ProductsMetaData.PRODUCT_WEIGHT + " text,"
			+ ProductsMetaData.PRODUCT_SIZE + " text,"
			+ ProductsMetaData.PRODUCT_IMG + " text,"
			+ ProductsMetaData.PRODUCT_LISTA + " double,"
			+ ProductsMetaData.PRODUCT_PRODUCTION_LEVEL + " int not null, "
			+ ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC + " double,"
			+ ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR + " double,"
			+ ProductsMetaData.PRODUCT_CROPID + " text,"
			+ ProductsMetaData.PRODUCT_CULTIVARID + " text);";

	//sql code for the creation of HISTORY table
	private static final String HISTORY_TABLE_CREATE = "CREATE TABLE "  
			+ HistoryMetaData.HISTORY_TABLE + " (" 
			+ HistoryMetaData.PRODUCT_HISTORY_ID + " text, "
			+ HistoryMetaData.PRODUCT_HISTORY_NAME + " text, "
			+ HistoryMetaData.PRODUCT_VARIETY + " text, "
			+ HistoryMetaData.PRODUCT_HISTORY_PRICE + " double,"
			+ HistoryMetaData.PRODUCT_HISTORY_IMG + " Varchar[12],"
			+ HistoryMetaData.PRODUCT_HISTORY_COLOR + " int,"
			+ HistoryMetaData.PRODUCT_HISTORY_ANNO + " int,"
			+ HistoryMetaData.PRODUCT_HISTORY_MESE + " int,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC + " double,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR + " double,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA + " double);";


	//classe che ci aiuta nella creazione del db
	private class DbHelper extends SQLiteOpenHelper { 

		public DbHelper(Context context, String name, CursorFactory factory,int version) {
			super(context, name, factory, version);
		}
		//solo quando il db viene creato, creiamo la tabella
		@Override
		public void onCreate(SQLiteDatabase _db) { 
			_db.execSQL(PRODUCTS_TABLE_CREATE);
			_db.execSQL(HISTORY_TABLE_CREATE);
			_db.execSQL(PRODUCTION_TABLE_CREATE);
		}


		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			//qui mettiamo eventuali modifiche al db, se nella nostra nuova versione della app, il db cambia numero di versione
		}

	}





}