package com.slk.application;



import java.util.ArrayList;
import java.util.List;

import com.slk.bean.Crop;
import com.slk.bean.Farm;
import com.slk.bean.Farmer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBAdapter 
{
	private static final String DATABASE_NAME = "slkfarm.db";
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase db;
	private final Context context;
	private DbHelper dbHelper;
	
	public DBAdapter(Context context) 
	{
		this.context = context;
		dbHelper = new DbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DBAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() 
	{
		dbHelper.close();
		db.close();
	}
	
	
	private static class DbHelper extends SQLiteOpenHelper 
	{
		public DbHelper(Context context, String name, CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			String sql = "";
			sql = "CREATE TABLE mobile(";
			sql = sql + "id INTEGER PRIMARY KEY AUTOINCREMENT,";
			sql = sql + "phoneNumber INTEGER NOT NULL)";
			db.execSQL(sql);
			sql = "";
			sql = sql + "CREATE TABLE farmer (";
			sql = sql + "id INTEGER PRIMARY KEY,";
			sql = sql + "firstname TEXT,";
			sql = sql + "lastname TEXT,";
			sql = sql + "email TEXT,";
			sql = sql + "pin TEXT,";
			sql = sql + "secretKey TEXT)";
			db.execSQL(sql);
			sql = "";
			sql = sql + "CREATE TABLE mobile_has_farmer(";
			sql = sql + "id INTEGER PRIMARY KEY AUTOINCREMENT,";
			sql = sql + "farmerId INTEGER REFERENCES farmer(id),";
			sql = sql + "phoneNumber INTEGER REFERENCES mobile(phoneNumber))";
			db.execSQL(sql);
			sql = "";
			sql = sql + "CREATE TABLE farm (";
			sql = sql + "id INTEGER PRIMARY KEY,";
			sql = sql + "name TEXT,";
			sql = sql + "agroRegion INTEGER,";
			sql = sql + "farmerId INTEGER REFERENCES farmer(id))";
			db.execSQL(sql);		
			sql = "";
			sql = sql + "CREATE TABLE crops (";
			sql = sql + "cultivarName TEXT PRIMARY KEY,";
			sql = sql + "cropName TEXT NOT NULL,";
			sql = sql + "cropType TEXT NOT NULL,";
			sql = sql + "maxProduction INTEGER NOT NULL,";
			sql = sql + "currentProduction INTEGER NOT NULL,";
			sql = sql + "percentageProduction REAL NOT NULL)";
			db.execSQL(sql);
			sql = "";
			sql = sql + "CREATE TABLE crops_for_region(";
			sql = sql + "cropId TEXT REFERENCES crops(cultivarName),";
			sql = sql + "regionId INTEGER REFERENCES farm(agroRegion),";
			sql = sql + "PRIMARY KEY (cropId, regionId))";
			db.execSQL(sql);
			sql = "";
			sql = sql + "CREATE TABLE crops_for_farm(";
			sql = sql + "farmId INTEGER REFERENCES farm(id),";
			sql = sql + "cropId TEXT REFERENCES crops(cultivarName),";
			sql = sql + "quantity INTEGER,";
			sql = sql + "PRIMARY KEY (farmId, cropId))";
			db.execSQL(sql);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			onCreate(db);
		}
	}
	
	
	public Farmer getFarmer(String pin, String phoneNumber)
	{
		String sql = "Select * from mobile INNER JOIN mobile_has_farmer ON (mobile.phoneNumber = mobile_has_farmer.phoneNumber)" +
				" INNER JOIN farmer ON (mobile_has_farmer.farmerId = farmer.id) " +
				"WHERE mobile_has_farmer.phoneNumber = '"+phoneNumber+"' AND farmer.pin ='"+pin+"'";
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount()>0)
		{
			c.moveToFirst();
			Farmer f = new Farmer();
			f.setId(c.getInt(c.getColumnIndex("id")));
			f.setFirstname(c.getString(c.getColumnIndex("firstname")));
			f.setLastname(c.getString(c.getColumnIndex("lastname")));
			f.setPin(pin);
			f.setEmail(c.getString(c.getColumnIndex("email")));
			f.setSecretKey(c.getString(c.getColumnIndex("secretKey")));
			c.close();
			Log.i("db", "return farmer ok");
			return f;
		}
		else
		{
			c.close();
			Log.i("db", "return farmer null");
			return null;
		}
	}
	
	public int updateSecretKey(int id, String secretKey)
	{
		ContentValues values = new ContentValues();
		values.put("secretKey", secretKey);
		Log.i("db", "updateKey");
		return db.update("farmer", values, "id = '"+String.valueOf(id)+"'", null);
	}
	
	public List<Farm> getFarmList(int farmerId)
	{
		String sql = "Select * from farm where farmerId ='"+String.valueOf(farmerId)+"'";
		Cursor c = db.rawQuery(sql, null);
		List<Farm> farmList = new ArrayList<Farm>();
		while (c.moveToNext())
		{
			Farm f = new Farm();
			f.setId(c.getInt(c.getColumnIndex("id")));
			f.setName(c.getString(c.getColumnIndex("name")));
			f.setAgroGeolocigalRegionId(c.getInt(c.getColumnIndex("agroRegion")));
			f.setFarmerId(farmerId);
			farmList.add(f);
		}
		c.close();
		Log.i("db", "get farmlist");
		return farmList;
	}
	
	public int register(Farmer f, List<Farm> farmList, String phoneNumber)
	{
		int result = 1;
		db.beginTransaction();
		try
		{
			ContentValues farmerValues = new ContentValues();
			farmerValues.put("id", f.getId());
			farmerValues.put("firstname", f.getFirstname());
			farmerValues.put("lastname", f.getLastname());
			farmerValues.put("pin", f.getPin());
			farmerValues.put("email", f.getEmail());
			farmerValues.put("secretKey", f.getSecretKey());
			long n = db.insert("farmer", null, farmerValues);
			Log.i("db", "insert farmer " + String.valueOf(n));
			
			for (int i=0; i<farmList.size(); i++)
			{
				Farm farm = farmList.get(i);
				ContentValues farmValues = new ContentValues();
				farmValues.put("id", farm.getId());
				farmValues.put("name", farm.getName());
				farmValues.put("agroRegion", farm.getAgroGeolocigalRegionId());
				farmValues.put("farmerId", farm.getFarmerId());
				n = db.insert("farm", null, farmValues);
				Log.i("db", "insert farm " +  String.valueOf(n));
			}
			
			ContentValues mobileValues = new ContentValues();
			mobileValues.put("phoneNumber", Integer.valueOf(phoneNumber));
			n = db.insert("mobile", null, mobileValues);
			Log.i("db", "insert mobile " +  String.valueOf(n));
			
			ContentValues mobileHasFarmer = new ContentValues();
			mobileHasFarmer.put("phoneNumber", Integer.valueOf(phoneNumber));
			mobileHasFarmer.put("farmerId", f.getId());
			n = db.insert("mobile_has_farmer", null, mobileHasFarmer);
			Log.i("db", "insert mobile_has_farmer " +  String.valueOf(n));
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			Log.e("db", "error transaction");
			result = 0;
		}
		finally
		{
			db.endTransaction();
			Log.i("db", "transaction ok");
		}	
		return result;
	}
	
	public boolean haveCropForAgroRegion(int agroRegion)
	{
		String sql = "Select * from crops_for_region where regionId = '"+String.valueOf(agroRegion)+"'";
		Cursor c = db.rawQuery(sql, null);
		int result = c.getCount();
		c.close();
		return result>0;
	}
	
	public int registerCrops(List<Crop> cropList, int agroRegion)
	{
		int result = 1;
		db.beginTransaction();
		try
		{
			for (int i=0; i<cropList.size(); i++)
			{
				Crop crop = cropList.get(i);
				ContentValues values = new ContentValues();
				values.put("cultivarName", crop.getCultivarName());
				values.put("cropName", crop.getCropName());
				values.put("cropType", crop.getCropType());
				values.put("maxProduction", crop.getMaxProduction());
				values.put("currentProduction", crop.getCurrentProduction());
				values.put("percentageProduction", crop.getPercentageProduction());
				String sql = "Select * from crops where cultivarName = '"+crop.getCultivarName()+"'";
				
				Cursor c = db.rawQuery(sql, null);
				if (c.getCount() == 0)
				{
					long n = db.insert("crops", null, values);
					Log.i("db", "insert crops " +  String.valueOf(n));
				}	
				else
				{
					c.moveToNext();
					long n = db.update("crops", values, "cultivarName = '"+crop.getCultivarName()+"'", null);
					Log.i("db", "update crops " +  String.valueOf(n));
				}	
				
				c.close();
				sql = "";
				sql = sql + "Select * from crops_for_region where cropId ='"+crop.getCultivarName()+"' and regionId ='"+String.valueOf(agroRegion)+"'";
				c = db.rawQuery(sql, null);
				if (c.getCount() == 0)
				{
					ContentValues cropsForRegion = new ContentValues();
					cropsForRegion.put("cropId", crop.getCultivarName());
					cropsForRegion.put("regionId", agroRegion);
					long n = db.insert("crops_for_region", null, cropsForRegion);
					Log.i("db", "insert crops_for_region " +  String.valueOf(n));
				}
				c.close();
				
			}
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			Log.e("db", "error transaction");
			result = 0;
		}
		finally
		{
			db.endTransaction();
			Log.i("db", "transaction ok");
		}	
		return result;
	}
	
	public List<Crop> getCropsList(int region, String type)
	{
		
		ArrayList<Crop> cropList = new ArrayList<Crop>();
		String sql = "Select * from crops_for_region INNER JOIN crops ON crops_for_region.cropId = crops.cultivarName where regionId = '"+String.valueOf(region)+"'" +
				" and cropType = '"+type+"' Order By PercentageProduction Asc";
		Cursor cursor = db.rawQuery(sql, null);
		Log.i("db", String.valueOf(cursor.getCount()));
		while (cursor.moveToNext())
		{
			Crop c = new Crop();
			c.setCultivarName(cursor.getString(cursor.getColumnIndex("cultivarName")));
			c.setCropName(cursor.getString(cursor.getColumnIndex("cropName")));
			c.setCropType(cursor.getString(cursor.getColumnIndex("cropType")));
			c.setMaxProduction(cursor.getInt(cursor.getColumnIndex("maxProduction")));
			c.setCurrentProduction(cursor.getInt(cursor.getColumnIndex("currentProduction")));
			c.setPercentageProduction(cursor.getDouble(cursor.getColumnIndex("percentageProduction")));
			cropList.add(c);
		}
		cursor.close();
		return cropList;
	}
	
	public List<Crop> getProductionList(int farmId)
	{
		String sql = "Select * from crops_for_farm INNER JOIN crops ON (crops_for_farm.cropId = crops.cultivarName) where crops_for_farm.farmId ='"+String.valueOf(farmId)+"'";
		Cursor cursor = db.rawQuery(sql, null);
		List<Crop> list = new ArrayList<Crop>();
		while (cursor.moveToNext())
		{
			Crop c = new Crop();
			c.setCultivarName(cursor.getString(cursor.getColumnIndex("cultivarName")));
			c.setCropName(cursor.getString(cursor.getColumnIndex("cropName")));
			c.setCropType(cursor.getString(cursor.getColumnIndex("cropType")));
			c.setMaxProduction(cursor.getInt(cursor.getColumnIndex("maxProduction")));
			c.setCurrentProduction(cursor.getInt(cursor.getColumnIndex("currentProduction")));
			c.setPercentageProduction(cursor.getDouble(cursor.getColumnIndex("percentageProduction")));
			c.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
			list.add(c);
		}
		cursor.close();
		Log.i("db", "get production list");
		return list;
	}
}
