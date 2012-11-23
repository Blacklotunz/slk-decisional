package com.slk.application;

import java.util.ArrayList;

import com.slk.R;
import com.slk.bean.Product;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


public class ColorSetter {

	private static final String UNDER_SUPPLY_0_11 = "#82FA58";
	private static final String UNDER_SUPPLY_12_22 = "#40FF00";
	private static final String UNDER_SUPPLY_23_33 = "#31B404";

	private static final String NORMAL_SUPPLY_34_45 = "#F7D358";
	private static final String NORMAL_SUPPLY_46_57 = "#FFBF00";
	private static final String NORMAL_SUPPLY_58_67 = "#B18904";

	private static final String OVER_SUPPLY_68_79 = "#FE2E2E";
	private static final String OVER_SUPPLY_80_91 = "#B40404";
	private static final String OVER_SUPPLY_92_100 = "#610B0B";


	public static int getColours(int level, int color_code) {
		int colorCode = 0;
		//sfumature di verde
		if (level<=11)
			colorCode = Color.parseColor(UNDER_SUPPLY_0_11);
		else if (level>=12 && level<=22)
			colorCode = Color.parseColor(UNDER_SUPPLY_12_22);
		else if (level>=23 && level<=33)
			colorCode = Color.parseColor(UNDER_SUPPLY_23_33);
		//sfumature di giallo
		if (level>=34 && level<=45)
			colorCode = Color.parseColor(NORMAL_SUPPLY_34_45);
		else if (level>=46 && level<=57)
			colorCode = Color.parseColor(NORMAL_SUPPLY_46_57);
		else if (level>=58 && level<=67)
			colorCode = Color.parseColor(NORMAL_SUPPLY_58_67);
		//sfumature di rosso	
		if (level>=68 && level<=79)
			colorCode = Color.parseColor(OVER_SUPPLY_68_79);
		else if (level>=80 && level<=91)
			colorCode = Color.parseColor(OVER_SUPPLY_80_91);
		else if (level>=92)
			colorCode = Color.parseColor(OVER_SUPPLY_92_100);

		return colorCode;
	}

	public static void setBgColor(Product p, View view){
		int level = p.getProductionLevel();
		//sfumature di verde
		if (level<=11)
			view.setBackgroundResource(R.drawable.lightgreenbuttons);
		else if (level>=12 && level<=22)
			view.setBackgroundResource(R.drawable.mediumgreenbuttons);
		else if (level>=23 && level<=33){
			view.setBackgroundResource(R.drawable.darkgreenbuttons);
		}
		//sfumature di giallo
		if (level>=34 && level<=45)
			view.setBackgroundResource(R.drawable.lightyellowbuttons);
		else if (level>=46 && level<=57)
			view.setBackgroundResource(R.drawable.mediumyellowbuttons);
		else if (level>=58 && level<=67)
			view.setBackgroundResource(R.drawable.darkyellowbuttons);
		//sfumature di rosso	
		if (level>=68 && level<=79)
			view.setBackgroundResource(R.drawable.lightredbuttons);
		else if (level>=80 && level<=91)
			view.setBackgroundResource(R.drawable.mediumredbuttons);
		else if (level>=92)
			view.setBackgroundResource(R.drawable.darkredbuttons);

	}
	
	public static void setWhiteTextCompare(Product p, ArrayList<TextView> vList){
		if(p.getProductionLevel()>=92){
			for(TextView v : vList)
				v.setTextColor(Color.WHITE);
		}
		else{
			for(TextView v : vList)
				v.setTextColor(Color.BLACK);
		}
	}

}