package com.slk.presentation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.http.HttpConnection;

import com.slk.application.DBAdapter;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class Base extends Activity 
{


	public static final String IMEI_EMULATOR = "000000000000000";
	public static final String IMEI_TEST = "359043047263857";
	public static final String IMAGES_PATH = "/data/data/it.slkfarm.android/files/";
	public static final String JSON_REGISTRATION = "Registration";
	public static final String JSON_LOGIN = "Login";
	public static final String JSON_LOGIN_ACTION = "Login";
	public static final String JSON_REGISTRATION_ACTION = "Regst";
	public static final String WEB_SERVICE_URL_STAGES = "http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/stages";
	public static final String WEB_SERVICE_URL_LOGIN= "http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/registration";
	public static final String WEB_SERVICE_URL_CROP = "http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/crop";
	public static final String WEB_SERVICE_URL_REGISTRATION= "http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/registration";
	public static final String TAG_STAGES = "Stages";
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "name";
	public static final String IMAGE_URL = "image_url";
	public static final String TAG_LOGIN = "login";
	public static final String TAG_REGISTER = "registerphonenumber";
	public static final String TAG_GET_CROPS = "getcrops";
	protected DBAdapter dbAdapter;
	public static boolean stopService = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		//startService(intent);		
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		dbAdapter.close();
		stopService = false;
	}
	
	public static boolean getStatus()
	{
		return stopService;
	}
	
	public void stop()
	{
		stopService = true;
	}
}