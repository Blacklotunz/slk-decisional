package com.slk.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class WSHandler {

	public void sendRequest(String httpsURL)
	{
		try {
			// Construct data
			String data = URLEncoder.encode("Tag", "UTF-8")+"="+URLEncoder.encode("getcrops", "UTF-8");

			// Send data
			URL url = new URL("http://webe1.scm.uws.edu.au/index.php/agriculture/web_services/index/crop");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
			//handle exception
		}
	}

}
