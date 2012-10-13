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
	}


	/* Con questo metodi si inseriscono i prodotti nel database
	 * name prende in input il nome del prodotto che è anche la chiave
	 * price il prezzo del prodotto
	 * img è il codice che corrisponde al file immagine del prodotto codificato da R
	 * lista è usata per distinguere i prodotti tra: verdi lista=1, gialli lista=2, rossi lista=3
	 * q_venduta_anno_prec e' la quantita di prodotto venduta l'anno precedente
	 * q_prev_anno_corr e' la quantita preventivata fino ad ora
	 * stagione distingue la stagione in cui si coltiva il prodotto: 1=primavera, 2=estate, 3=autunno, 4=inverno
	 * */
	public void insertProduct(String name,double price,int img,int lista,int q_venduta_anno_prec,int q_prev_anno_corr,int stagione){ //metodo per inserire i dati
		ContentValues cv=new ContentValues();
		cv.put(ProductsMetaData.PRODUCT_NAME, name);
		cv.put(ProductsMetaData.PRODUCT_PRICE, price);
		cv.put(ProductsMetaData.PRODUCT_IMG, img);
		cv.put(ProductsMetaData.PRODUCT_LISTA, lista);
		cv.put(ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC, q_venduta_anno_prec);
		cv.put(ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		cv.put(ProductsMetaData.PRODUCT_STAGIONE, stagione);
		mDb.insert(ProductsMetaData.PRODUCTS_TABLE, null, cv);
	}
	/*Metodo per incrementare la quantità di prodotto dopo che è stata modificata nella schermata finale*/
	public void updateProduct(String name,int q_prev_anno_corr){
		String strFilter = "name='" + name+"'";
		ContentValues args = new ContentValues();
		args.put(ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		mDb.update("products", args, strFilter, null);	
	}

	public void updateListaProduct(String name, int lista) {
		String strFilter = "name='" + name+"'";
		ContentValues args = new ContentValues();
		args.put(ProductsMetaData.PRODUCT_LISTA, lista);
		mDb.update("products", args, strFilter, null);	

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
	public void insertProductInHistory(String name,double price,int img,int colore,int anno,int mese,int q_venduta_anno_prec,int q_prev_anno_corr,int stagione,int q_prodotta){
		ContentValues cv=new ContentValues();
		cv.put(HistoryMetaData.PRODUCT_HISTORY_NAME, name);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_PRICE, price);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_IMG, img);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_COLOR, colore);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_ANNO, anno);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_MESE, mese);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC, q_venduta_anno_prec);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR, q_prev_anno_corr);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_STAGIONE, stagione);
		cv.put(HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA, q_prodotta);
		mDb.insert(HistoryMetaData.HISTORY_TABLE, null, cv);
	}

	public void updateProductInHistory(String name,int q_prodotta){
		String strFilter = "name='" + name+"' and anno='2012'";
		ContentValues args = new ContentValues();
		args.put(HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA, q_prodotta);
		mDb.update("history", args, strFilter, null);	
	}
	public void updateProductColorInHistory(String name,int colore){
		String strFilter = "name='" + name+"' and anno='2012'";
		ContentValues args = new ContentValues();
		args.put(HistoryMetaData.PRODUCT_HISTORY_COLOR, colore);
		mDb.update("history", args, strFilter, null);	
	}

	/*Metodo che ritorna il prodotto passato da name in un determinato anno(se è presente)*/
	public Cursor getHistoryProductbyYear(String name,int anno){ 
		return mDb.query(HistoryMetaData.HISTORY_TABLE, null,HistoryMetaData.PRODUCT_HISTORY_NAME+"='" + name + "'"+" and "+HistoryMetaData.PRODUCT_HISTORY_ANNO+"='" + anno + "'",null,null,null,null);                            
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla tabella history
	 *ordinati in base all anno*/	
	public Cursor fetchHistoryProducts(){ 
		return mDb.query(HistoryMetaData.HISTORY_TABLE, null,null,null,null,null,"anno");               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *contenuti nella tabella products*/
	public Cursor fetchProducts(){
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,null,null,null,null,"q_vend_anno_prec-q_prev_anno_corr");               
	}
	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria sottoproduzione(verdi)
	 *ordinati in base al prezzo*/	
	public Cursor fetchGreenProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==1",null,null,null,"q_vend_anno_prec-q_prev_anno_corr");               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria mediaproduzione(gialli)
	 *ordinati in base al prezzo*/	
	public Cursor fetchYellowProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==2",null,null,null,"q_vend_anno_prec-q_prev_anno_corr");               
	}

	/*Metodo che restituisce tutti gli elementi 
	 *appartenenti alla categoria sovrapproduzione(rossi)
	 *ordinati in base al prezzo*/	
	public Cursor fetchRedProducts(){ 
		return mDb.query(ProductsMetaData.PRODUCTS_TABLE, null,ProductsMetaData.PRODUCT_LISTA+"==3",null,null,null,"q_vend_anno_prec-q_prev_anno_corr");               
	}

	// i metadati della tabella products, accessibili ovunque
	static public class ProductsMetaData {  
		static public final String PRODUCTS_TABLE = "products";
		static public final String PRODUCT_NAME = "name";
		static public final String PRODUCT_PRICE = "price";
		static public final String PRODUCT_IMG = "immagine";
		static public final String PRODUCT_LISTA = "app_lista";
		static public final String PRODUCT_QUANT_VEND_ANNO_PREC = "q_vend_anno_prec";
		static public final String PRODUCT_QUANT_PREV_ANNO_CORR = "q_prev_anno_corr";
		static public final String PRODUCT_STAGIONE =	"stagione";
	}

	// i metadati della tabella HISTORY, accessibili ovunque
	static public class HistoryMetaData {  
		static final String ID = "_id";
		static public final String HISTORY_TABLE = "history";
		static public final String PRODUCT_HISTORY_NAME = "name";
		static public final String PRODUCT_HISTORY_PRICE = "price";
		static public final String PRODUCT_HISTORY_IMG = "immagine";
		static public final String PRODUCT_HISTORY_COLOR = "colore";
		static public final String PRODUCT_HISTORY_ANNO = "anno";
		static public final String PRODUCT_HISTORY_MESE = "mese";
		static public final String PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC = "q_vend_anno_prec";
		static public final String PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR = "q_prev_anno_corr";
		static public final String PRODUCT_HISTORY_STAGIONE =	"stagione";
		static public final String PRODUCT_HISTORY_QUANTIT_PRODOTTA =	"q_prodotta";
	}

	
	//sql code for the creation of PRODUCTS table
	private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  
			+ ProductsMetaData.PRODUCTS_TABLE + " (" 
			+ ProductsMetaData.PRODUCT_NAME + " text primary key, "
			+ ProductsMetaData.PRODUCT_PRICE + " double not null,"
			+ ProductsMetaData.PRODUCT_IMG + " Varchar[12] not null,"
			+ ProductsMetaData.PRODUCT_LISTA + " int not null,"
			+ ProductsMetaData.PRODUCT_QUANT_VEND_ANNO_PREC + " int not null,"
			+ ProductsMetaData.PRODUCT_QUANT_PREV_ANNO_CORR + " int not null,"
			+ ProductsMetaData.PRODUCT_STAGIONE + " int not null);";

	//sql code for the creation of HISTORY table
	private static final String HISTORY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  
			+ HistoryMetaData.HISTORY_TABLE + " (" 
			+ HistoryMetaData.ID+ " integer primary key autoincrement, "
			+ HistoryMetaData.PRODUCT_HISTORY_NAME + " text not null, "
			+ HistoryMetaData.PRODUCT_HISTORY_PRICE + " double not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_IMG + " Varchar[12] not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_COLOR + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_ANNO + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_MESE + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANT_VEND_ANNO_PREC + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANT_PREV_ANNO_CORR + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_STAGIONE + " int not null,"
			+ HistoryMetaData.PRODUCT_HISTORY_QUANTIT_PRODOTTA + " int not null);";


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
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			//qui mettiamo eventuali modifiche al db, se nella nostra nuova versione della app, il db cambia numero di versione
		}

	}





}