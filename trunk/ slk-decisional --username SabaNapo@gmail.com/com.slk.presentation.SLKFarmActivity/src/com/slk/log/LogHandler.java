package com.slk.log;

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
import android.widget.Toast;

public class LogHandler extends Activity{

	public static void appendLog(String text)
        {       
                try {
                        //append timestamp to text
                        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                        String timestamp = s.format(new Date());

                        String textt = timestamp+" : "+text+" \n";


                        File storagePath = Environment.getExternalStorageDirectory(); 
                        SimpleDateFormat s2 = new SimpleDateFormat("dd-MM-yyyy");
                        String date = s2.format(new Date());
                        FileWriter fw = new FileWriter(new File(storagePath,"SLKlog"+date+".txt"), true);       
                        
                        fw.append(textt);
                        fw.flush();
                        fw.close();
                        
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public static Intent getMailIntent(){
        	Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/txt");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"sabanapo@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "SLK Log");
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
            i.putExtra(Intent.EXTRA_TEXT   , "Log Date:"+s.format(new Date())+". \n For more information look at the attached file.");
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/SLKlog"+s.format(new Date())+".txt"));
            return i;
        }
        
        /**
         * example of how to create e.mail. this methos will not be used
         */
        private void sendLog(){
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("application/txt");
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