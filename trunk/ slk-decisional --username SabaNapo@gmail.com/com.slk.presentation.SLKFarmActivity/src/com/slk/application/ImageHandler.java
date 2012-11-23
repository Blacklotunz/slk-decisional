package com.slk.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.content.Context;


public class ImageHandler {

	/**
	 * @method this method download an image from a given URL and give it the given name saving it on external SD card.
	 * @param String url: the url of the image.
	 * @param String imageName: the name of we want to give at image without extension. (set it as the product ID).
	 * @throws IOException 
	 */
	
	public static void downloadImageFromUrl(String urll, String imgName, Context c) throws IOException{
		urll = urll.replace("\\", "/");
		
		URL url = new URL (urll);
		InputStream input = url.openStream();
		try {
			//The sdcard directory e.g. '/sdcard' can be used directly, or 
			//more safely abstracted with getExternalStorageDirectory()
			//File storagePath = Environment.getExternalStorageDirectory();
			
			File storagePath = c.getDir("images", Context.MODE_PRIVATE); //Creating an internal dir;
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
	
	public static File loadImage(Context c, String imgName){
		
		File storagePath = c.getDir("images", Context.MODE_PRIVATE); //Creating an internal dir;
		return new File(storagePath,imgName+".png");
	}
}
