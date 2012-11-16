package com.slk.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class LogHandler extends Activity{

	private static String log="";
	

	public String getLog(){
		return log;
	}
	
	public void appendLog(String text)
	{       
		try {
			//append timestamp to text
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String timestamp = s.format(new Date());

			String textt = timestamp+" : "+text+" \n";


			File storagePath = Environment.getExternalStorageDirectory();
			FileWriter fw = new FileWriter(new File(storagePath,"SLKlog.txt"), true);	
			
			fw.append(textt);
			fw.flush();
			fw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteLog(){
		File file = new File(Environment.getExternalStorageDirectory(),"SLKlog.txt");
		file.delete();
	}

	/**
	 * example of how to create e.mail. this methos will not be used
	 */
	private void sendLog(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("audio/mp3");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sabanapo@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
		i.putExtra(Intent.EXTRA_TEXT   , "look at the attached file!");
		i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/SLKlog.txt"));
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

}
