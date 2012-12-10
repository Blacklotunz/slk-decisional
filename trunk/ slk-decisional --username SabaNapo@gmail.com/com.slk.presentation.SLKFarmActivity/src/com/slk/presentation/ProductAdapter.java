package com.slk.presentation;

import com.slk.R;
import com.slk.application.ImageHandler;
import com.slk.bean.Product;
import com.slk.log.LogHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends ArrayAdapter<Product>{

	Context context; 
	int layoutResourceId;    
	Product data[] = null;
	View row = null;
	Product product;
	ProductHolder holder = null;

	public ProductAdapter(Context context, int layoutResourceId, Product[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		row = convertView;	
		product = data[position];
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ProductHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.image);
			holder.txtTitle = (TextView)row.findViewById(R.id.text);
			holder.cb = (CheckBox)row.findViewById(R.id.checkBox);
			holder.row = (View)row.findViewById(R.id.listrow);
			holder.crop = product;
			row.setTag(holder);
	
		}
		else
		{
			holder = (ProductHolder)row.getTag();
		}
		
		holder.txtTitle.setText(product.getName()+" "+product.getVariety()+" ");
		holder.txtTitle.setTextColor(context.getResources().getColor(R.color.text));
		Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImage(context, product.getId()).getAbsolutePath());
		if(bitmap==null){
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		}
		holder.imgIcon.setImageBitmap(bitmap);
		holder.row.setBackgroundColor(product.getColor());
		if(product.getColor() == -10417397){
			holder.txtTitle.setTextColor(context.getResources().getColor(R.color.White));
		}
		row.setOnClickListener(new OnItemClickListener(position, data, holder.cb,context));
		return row;
	}

	static class ProductHolder
	{
		ImageView imgIcon;
		TextView txtTitle;
		CheckBox cb;
		View row;
		Product crop;
		
	}
	
}
