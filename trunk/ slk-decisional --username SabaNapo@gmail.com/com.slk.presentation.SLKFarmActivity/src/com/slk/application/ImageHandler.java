package com.slk.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


public class ImageHandler {

	/**
	 * @method this method download an image from a given URL and give it the given name saving it on external SD card.
	 * @param String url: the url of the image.
	 * @param String imageName: the name of we want to give at image without extension. (set it as the product ID).
	 * @throws IOException 
	 */
	public static void downloadImageFromUrl(String urll, String imgName) throws IOException{
		URL url = new URL (urll);
		InputStream input = url.openStream();
		try {
			//The sdcard directory e.g. '/sdcard' can be used directly, or 
			//more safely abstracted with getExternalStorageDirectory()
			File storagePath = Environment.getExternalStorageDirectory();
			Log.i("storage path", storagePath.getAbsolutePath());
			OutputStream output = new FileOutputStream (new File(storagePath,imgName+".png"));
			try {
				byte[] buffer = new byte[8192];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					output.write(buffer, 0, bytesRead);
				}
			} finally {
				output.close();
			}
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
