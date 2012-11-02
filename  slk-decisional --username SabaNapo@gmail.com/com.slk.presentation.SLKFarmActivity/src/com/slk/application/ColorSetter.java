package com.slk.application;

import java.util.ArrayList;

import com.slk.bean.Product;

import android.graphics.Color;


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
		Color color = new Color();
		 //sfumature di verde
			if (level<=11)
				colorCode = color.parseColor(UNDER_SUPPLY_0_11);
			else if (level>=12 && level<=22)
				colorCode = color.parseColor(UNDER_SUPPLY_12_22);
			else if (level>=23 && level<=33)
				colorCode = color.parseColor(UNDER_SUPPLY_23_33);
		 //sfumature di giallo
			if (level>=34 && level<=45)
				colorCode = color.parseColor(NORMAL_SUPPLY_34_45);
			else if (level>=46 && level<=57)
				colorCode = color.parseColor(NORMAL_SUPPLY_46_57);
			else if (level>=58 && level<=67)
				colorCode = color.parseColor(NORMAL_SUPPLY_58_67);
		 //sfumature di rosso	
			if (level>=68 && level<=79)
				colorCode = color.parseColor(OVER_SUPPLY_68_79);
			else if (level>=80 && level<=91)
				colorCode = color.parseColor(OVER_SUPPLY_80_91);
			else if (level>=92)
				colorCode = color.parseColor(OVER_SUPPLY_92_100);
			
		return colorCode;
	}

}