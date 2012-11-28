package com.slk.presentation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.slk.R;
import com.slk.application.DialogBuilder;
import com.slk.bean.Farm;
import com.slk.bean.Farmer;
import com.slk.http.HttpConnector;
import com.slk.storage.SLKStorage;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Login extends Base implements OnClickListener, Runnable
{
	private Button login, register, exit;
	private EditText pin, phone;
	private DialogBuilder builder;
	private Dialog dialog, progress;
	private TextView labelProgress;
	private Farmer farmer;
	private JSONObject farm;
	private boolean farmRegister;
	private List<Farm> farmList;
	private String resultPin, resultPhoneNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.i("activity","create");

		SLKStorage db = new SLKStorage(this);
		db.clear();

		farmer = new Farmer();
		setContentView(R.layout.login);

		TextView tv = (TextView) findViewById(R.id.tvLoginTitle);

		login = (Button) findViewById(R.id.btnLogin);

		login.setOnClickListener(this);

		pin = (EditText) findViewById(R.id.etPinLogin);
		phone = (EditText) findViewById(R.id.etPhoneLogin);
	}

	public void onClick(View v) 
	{
		if (v.getId() == login.getId())
		{
			resultPin = pin.getText().toString();
			resultPhoneNumber = phone.getText().toString();

			HttpConnector.num = resultPhoneNumber;
			HttpConnector.pin = resultPin;

			//CONTROLLO LUNGHEZZA SU ENTRAMBI I CAMPI
			if (resultPin.length() < 4 && resultPhoneNumber.length() < 9)
			{
				builder = new DialogBuilder(Login.this);
				Toast t = builder.createToast(R.string.error_length_pin_phone, null, this);
				t.show();
			}
			else
			{
				if (resultPin.length() < 4)
				{
					builder = new DialogBuilder(Login.this);
					Toast t = builder.createToast(R.string.error_length_pin, null, this);
					t.show();
				}
				else if (resultPhoneNumber.length() < 9)
				{
					builder = new DialogBuilder(Login.this);
					Toast t = builder.createToast(R.string.error_length_phone, null, this);
					t.show();

				}
				else
				{
					builder = new DialogBuilder(Login.this);
					progress = builder.createProgressDialog();
					labelProgress = (TextView) progress.findViewById(R.id.tvCustomProgressLabel);
					progress.show();
					new Thread(this).start();		
				}
			}
		}
		else if (v.getId() == register.getId())
		{

			//			Intent i = new Intent(Login.this,Registration.class);
			//			startActivity(i);
			//			finish();
		}
		else if (v.getId() == exit.getId())
		{
			stop();
			finish();
		}

	}

	private Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			String message = (String) msg.obj;
			if (message.equalsIgnoreCase("START"))
			{
				labelProgress.setText(R.string.waiting);
			}
			else if (message.equalsIgnoreCase("NO_CONNECTION"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_noconnection, null, null);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("SERVER_NO"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_server_no, null, null);
				dialog.show();
			}

			else if (message.equalsIgnoreCase("LOGIN_ERROR_PHONE"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_login_error_phone, null, null);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("LOGIN_ERROR_PIN"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_login_error_pin, null, null);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("LOADING_FARM"))
			{
				progress.dismiss();
				dialog = builder.creatSelectDialog(farmList, farmer, Login.this);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("ERROR_UPDATE_KEY"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_error_update_key, null, null);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("ERROR_GET_FARM"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_error_get_farm, null, null);
				dialog.show();
			}
			else if (message.equalsIgnoreCase("ERROR_STORAGE"))
			{
				progress.dismiss();
				dialog = builder.createDialog(R.string.error, R.string.handler_error_storage, null, null);
				dialog.show();
			}
		}
	};    

	public void run() 
	{
		sendHandlerMessage("START");
		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		//Controllo Connessione
		ConnectivityManager conManager = ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE));
		boolean isWifiEnabled = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		boolean isMobileEnabled = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
		if (!isWifiEnabled && !isMobileEnabled)
		{
			sendHandlerMessage("NO_CONNECTION");
		}
		else
		{	
			HttpConnector connector = new HttpConnector();
			try 
			{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("tag", TAG_LOGIN));
				nameValuePairs.add(new BasicNameValuePair("phonenumber",resultPhoneNumber));
				nameValuePairs.add(new BasicNameValuePair("pin",resultPin));
				if (connector.connect(WEB_SERVICE_URL_LOGIN, nameValuePairs) == 200)
				{
					JSONObject json = connector.getJson();
					//Log.i("json", json.toString());
					int error = json.getInt("error");
					if (error == 0)
					{
						farmer = dbAdapter.getFarmer(resultPin, resultPhoneNumber);
						if (farmer == null)
						{
							//Log.i("login", "no register");
							farmer = new Farmer();
							//FARMER NON REGISTRATO
							//LEGGO I DATI DEL FARMER DA JSON
							JSONObject farmerJson = json.getJSONObject("user").getJSONObject("farmer");
							Iterator iterator = farmerJson.keys();
							while(iterator.hasNext())
							{
								String key = (String) iterator.next();
								Log.i("key", key);
								if (key.equalsIgnoreCase("id"))
									farmer.setId(farmerJson.getInt(key));
								else if (key.equalsIgnoreCase("firstname"))
									farmer.setFirstname(farmerJson.getString(key));
								else if (key.equalsIgnoreCase("lastname"))
									farmer.setLastname(farmerJson.getString(key));
								else if (key.equalsIgnoreCase("secretkey"))
									farmer.setSecretKey(farmerJson.getString(key));
								else if (key.equalsIgnoreCase("email"))
									farmer.setSecretKey(farmerJson.getString(key));
							}
							farmer.setPin(resultPin);
							farmList = new ArrayList<Farm>();
							JSONObject listFarm = json.getJSONObject("user").getJSONObject("farm");
							Iterator keyFarm = listFarm.keys();
							int i=0;
							while (keyFarm.hasNext())
							{
								String dynamicKey = (String) keyFarm.next();
								JSONObject farm = listFarm.getJSONObject(dynamicKey);
								Farm f = new Farm();
								f.setId(farm.getInt("id"));
								f.setName(farm.getString("name"));
								f.setAgroGeolocigalRegionId(farm.getInt("agro_ecological_regions_id"));
								f.setFarmerId(farmer.getId());
								farmList.add(f);
							}

							//MEMORIZZO FARMER, FARMLIST E NUMERO DI TELEFONO
							if (dbAdapter.register(farmer, farmList, resultPhoneNumber) != 0)
							{

								sendHandlerMessage("LOADING_FARM");
							}
							else
							{

								sendHandlerMessage("ERROR_STORAGE");
							}	
						}
						else
						{
							Log.i("login", "register");
							//FARM REGISTRATO
							JSONObject farmerJson = json.getJSONObject("user").getJSONObject("farmer");
							String secretKey = farmerJson.getString("secretkey");
							Log.i("secretKey", secretKey);
							int n = dbAdapter.updateSecretKey(farmer.getId(), secretKey);
							Log.i("result update", String.valueOf(n));
							if (n != -1)
							{

								//UPDATE OK
								//LISTA DELLE FARM
								farmer.setSecretKey(secretKey);
								farmList = dbAdapter.getFarmList(farmer.getId());
								if (farmList.size() == 0)
								{
									//ERRORE RECUPERO FARM

									sendHandlerMessage("ERROR_GET_FARM");
								}
								else
								{

									sendHandlerMessage("LOADING_FARM");
								}
							}
							else
							{
								//UPDATE NO

								sendHandlerMessage("ERROR_UPDATE_KEY");
							}

						}
					}
					else if (error == 2)
					{
						//ERRORE PHONE NUMBER

						sendHandlerMessage("LOGIN_ERROR_PHONE");
					}
					else if (error == 3)
					{
						//ERRORE PIN

						sendHandlerMessage("LOGIN_ERROR_PIN");
					}
				}
				else
				{

					sendHandlerMessage("SERVER_NO");
				}
			} 
			catch (ClientProtocolException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}	
	}  

	public void sendHandlerMessage(String message)
	{
		Message msg = handler.obtainMessage();
		msg.obj = message;
		handler.sendMessage(msg);
	}


}
