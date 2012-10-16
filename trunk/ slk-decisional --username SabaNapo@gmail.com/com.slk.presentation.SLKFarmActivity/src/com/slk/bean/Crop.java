package com.slk.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Crop implements Serializable
{
	private static final long serialVersionUID = 7465804416814404007L;
	private String cropId;
	private String cropName;
	private String cropVariety;
	private int productionLevel;
	private double currentPrice;
	private Double[] lastYearSp;
	
	public String getCropId() {
		return cropId;
	}
	public void setCropId(String cropId) {
		this.cropId = cropId;
	}
	public String getCropName() {
		return cropName;
	}
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}
	public String getCropVariety() {
		return cropVariety;
	}
	public void setCropVariety(String cropVariety) {
		this.cropVariety = cropVariety;
	}
	public int getProductionLevel() {
		return productionLevel;
	}
	public void setProductionLevel(int productionLevel) {
		this.productionLevel = productionLevel;
	}
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public Double[] getLastYearSp() {
		return lastYearSp;
	}
	public void setLastYearSp(Double[] lastYearSp) {
		this.lastYearSp = lastYearSp;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
