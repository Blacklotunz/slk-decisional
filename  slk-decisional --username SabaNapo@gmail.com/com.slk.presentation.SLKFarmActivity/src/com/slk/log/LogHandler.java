package com.slk.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class LogHandler{
	private static File cacheFile;
	//private static String logg="";

	public static void appendLog(String text)
	{       
		try {
			//append timestamp to text
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String timestamp = s.format(new Date());
			String textt = timestamp+" : "+text+" \n";
			FileWriter fw = new FileWriter(cacheFile, true);       
			//logg=logg+textt;
			fw.append(textt);
			fw.flush();
			fw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	public static void createCachedFile(Context context, String fileName,String content) throws IOException {
		//logg = "";
		//logg = logg+content;
		
		cacheFile = new File(context.getFilesDir() + File.separator+ fileName);
		Log.v("path",cacheFile.getAbsolutePath()+"");
		//IF FILE already exist it'll be deleted and create a new copy
		if(!cacheFile.createNewFile()){
			cacheFile.delete();
			cacheFile.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(cacheFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF8");
		PrintWriter pw = new PrintWriter(osw);

		pw.println(content);

		pw.flush();
		pw.close();
	}

	public static Intent getSendEmailIntent(Context context, String email,String subject, String body, String fileName) {

		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		//Explicitly only use Gmail to send no more supported byGmail
		//emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");

		emailIntent.setType("plain/text");

		//Add the recipients
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		//emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, logg);
		
		//Add the attachment by specifying a reference to our custom ContentProvider
		//and the specific file of interest
		emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"+ fileName));
Log.v("sadsadasd", Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"+ fileName)+"");
		
		return emailIntent;
	}

	//return the intent with attached log file.
	public static Intent getMailIntent(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"slkfarmlogger@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "SLK Log");
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
		i.putExtra(Intent.EXTRA_TEXT   , "Log Date:"+s.format(new Date())+". \n For more information look at the attached file.");
		i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/SLKlog"+s.format(new Date())+".txt"));
		return i;
	}

	public static void sendMail(Context c) {
		Intent mailIntent = LogHandler.getSendEmailIntent(c,"slkfarmlogger@gmail.com", "Log","See attached file", "Log.txt");
		c.startActivity(Intent.createChooser(mailIntent, "Sending mail..."));		
	}
}