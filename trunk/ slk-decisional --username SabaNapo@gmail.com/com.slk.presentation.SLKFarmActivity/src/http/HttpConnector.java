package http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

}
