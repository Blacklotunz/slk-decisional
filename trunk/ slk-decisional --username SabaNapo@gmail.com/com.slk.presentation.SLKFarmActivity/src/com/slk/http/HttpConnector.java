package com.slk.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpConnector 
{
	public static String num, pin, farmId, secretkey;

	private InputStream inputStream; 	
	private JSONObject jsonObject;
	private String jsonString;
	private DefaultHttpClient client;
	private HttpPost post;
	private HttpResponse response;
	private HttpEntity entity;
	private BufferedReader reader;

	public HttpConnector()
	{
		client = new DefaultHttpClient();
	}


	public int connect(String URL, List<NameValuePair> variables) throws ClientProtocolException, IOException
	{
		post = new HttpPost(URL);
		if (variables != null) 
			post.setEntity(new UrlEncodedFormEntity(variables));
		response = client.execute(post);
		return response.getStatusLine().getStatusCode();
	}

	public int login(String URL, String phoneNumber, String pin) throws ClientProtocolException, IOException
	{
		post = new HttpPost(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("tag","login"));
		nameValuePairs.add(new BasicNameValuePair("phonenumber",phoneNumber));
		nameValuePairs.add(new BasicNameValuePair("pin",pin));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		response = client.execute(post);
		return response.getStatusLine().getStatusCode();
	}

	public int getCrops(String URL, String secretkey, String farmid) throws ClientProtocolException, IOException
	{
		post = new HttpPost(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("tag","getcropstest"));
		nameValuePairs.add(new BasicNameValuePair("secretkey",secretkey));
		nameValuePairs.add(new BasicNameValuePair("farmid",farmid));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		response = client.execute(post);
		return response.getStatusLine().getStatusCode();
	}


	public JSONObject getJson() throws IllegalStateException, IOException, JSONException
	{
		entity = response.getEntity();
		inputStream = entity.getContent();
		reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) 
		{
			sb.append(line + "\n");
		}
		inputStream.close();
		reader.close();
		jsonString = sb.toString();
		Log.i("HttpConnector",jsonString);
		jsonObject = new JSONObject(jsonString);
		return jsonObject;
	}

	public ArrayList<String> getFarms() throws ClientProtocolException, IllegalStateException, IOException, JSONException{
		ArrayList<String>farms = new ArrayList<String>();

		HttpConnector http = new HttpConnector();

		//delete this call when test will finish
		if(http.login("http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/registration", num, pin)==200){

			JSONObject jsonObj = http.getJson();
			if(jsonObj.getInt("success")==1){
				JSONObject farmss = jsonObj.getJSONObject("user").getJSONObject("farm");
				int size = farmss.names().length();

				for(int i=0;i<size;i++){
					farms.add((String)farmss.names().get(i));
				}
				Log.i("farms", farms.toString());
			}
			else{
				Log.e("HttpConnector.java", "not successfull operation");
			}
		}
		return farms;
	}

	//fetch all products now!!
	public ArrayList<JSONObject> fetchProducts() throws ClientProtocolException, IllegalStateException, IOException, JSONException {
		ArrayList<JSONObject> products = new ArrayList<JSONObject>();
		HttpConnector http = new HttpConnector();
		
		Log.i("HttpConnector.java", "secretkey = "+secretkey);

		if(http.getCrops("http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/crop", secretkey, farmId)==200){
			JSONObject jsonObject= http.getJson();
			products = getProductListFromJSONTEST(jsonObject);
		}else
			Log.e("HttpConnector.java", "not successfull operation");
		return products;
	}

	private ArrayList<JSONObject> getProductListFromJSON(JSONObject jsonObject){
		ArrayList<JSONObject> products = new ArrayList<JSONObject>();
		try {
			JSONObject cropInfo = jsonObject.getJSONObject("cropInfo");
			JSONObject vegetable= cropInfo.getJSONObject("Vegetable");					
			int i,j;
			int size = vegetable.names().length();
			for(i=0;i<size;i++){
				//get the legth of each vegetable
				Log.i("variety", "i="+i+" variety="+(String)vegetable.names().get(i));
				JSONObject variety = vegetable.getJSONObject((String)vegetable.names().get(i));
				int k = variety.names().length();
				//get each vegetable
				for(j=0;j<k;j++){
					products.add(variety.getJSONObject((String)variety.names().get(j)));
					Log.i("product",""+variety.getJSONObject((String)variety.names().get(j)));
				}
			}
			Log.i("products", products.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						

		return products;
	}

	//method to get products from test JSONObject
	private ArrayList<JSONObject> getProductListFromJSONTEST(JSONObject jsonObject){
		ArrayList<JSONObject> products = new ArrayList<JSONObject>();

		try {
			JSONObject cropInfo = jsonObject.getJSONObject("cropInfo");
			ArrayList<String>cropTypes = new ArrayList<String>();
			Iterator keys =cropInfo.keys();
			//itera tutte le tipologie di prodotti
			while(keys.hasNext()){
				String s = keys.next().toString();
				cropTypes.add(s);
			}
			for(String crop : cropTypes){
				JSONObject vegetable= cropInfo.getJSONObject(crop);				
				int i,j;
				int size = (vegetable.names().length());
				for(i=0;i<size;i++){
					//get the length of each vegetable
					if(!((String)vegetable.names().get(i)).equalsIgnoreCase("label")){
						JSONObject variety = vegetable.getJSONObject((String)vegetable.names().get(i));
						JSONObject cultivar = variety.getJSONObject("cultivar");
						int k = cultivar.names().length();
						Log.i("cultivar names", ""+cultivar.names());
						//get each vegetable
						for(j=0;j<k;j++){
							products.add(cultivar.getJSONObject((String)cultivar.names().get(j)));
							Log.i("product",""+cultivar.getJSONObject((String)cultivar.names().get(j)));
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}						
		return products;
	}

	public Map<String,String> insertProduction(Double quantity, String cropid, String cultivarid){
		post = new HttpPost("http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/crop");
		Map<String,String> toReturn = new HashMap<String,String>();
		
		//Tag – “production” 
		//secretkey, control,farmid – quantity, cropid, cultivarid, timestamp
		Date timestamp = new Date();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
		nameValuePairs.add(new BasicNameValuePair("tag","production"));
		nameValuePairs.add(new BasicNameValuePair("secretkey",secretkey));
		nameValuePairs.add(new BasicNameValuePair("farmid",farmId));
		nameValuePairs.add(new BasicNameValuePair("control","insert"));
		nameValuePairs.add(new BasicNameValuePair("quantity",quantity+""));
		nameValuePairs.add(new BasicNameValuePair("cropid",cropid));
		nameValuePairs.add(new BasicNameValuePair("cultivarid",cultivarid));
		nameValuePairs.add(new BasicNameValuePair("cultivarid",cultivarid));
		nameValuePairs.add(new BasicNameValuePair("timestamp",timestamp.getTime()+""));

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode()==200){
				//OK
				JSONObject json = getJson();
				if(!(json.getString("error").equalsIgnoreCase("1"))){
					
				toReturn.put("production_id",json.getString("production_id"));
				toReturn.put("production_quantity",json.getString("production_quantity"));
				toReturn.put("farm_id",json.getString("farm_id"));
				}
				else{
					Log.e("error", "1");
				}
			}
			else{
				Log.e("error", "http error");
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}




	public Map<String,String> updateProduction(Double quantity, String productionid){

		post = new HttpPost("http://webe1.scem.uws.edu.au/index.php/agriculture/web_services/index/crop");
		Map<String,String> toReturn = new HashMap<String,String>();
		
		//Tag – “production” 
		//secretkey, control,farmid – quantity, productionid
		Date timestamp = new Date();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
		nameValuePairs.add(new BasicNameValuePair("tag","production"));
		nameValuePairs.add(new BasicNameValuePair("secretkey",secretkey));
		nameValuePairs.add(new BasicNameValuePair("farmid",farmId));
		nameValuePairs.add(new BasicNameValuePair("control","update"));
		nameValuePairs.add(new BasicNameValuePair("quantity",quantity+""));
		nameValuePairs.add(new BasicNameValuePair("productionid",productionid));

		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode()==200){
				//OK
				JSONObject json = getJson();
				if(!(json.getString("error").equalsIgnoreCase("1"))){
					
				toReturn.put("production_id",json.getString("production_id"));
				toReturn.put("production_quantity",json.getString("production_quantity"));
				toReturn.put("farm_id",json.getString("farm_id"));
				}
				else{
					Log.e("error", "1");
				}
			}
			else{
				Log.e("error", "http error");
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}

}
