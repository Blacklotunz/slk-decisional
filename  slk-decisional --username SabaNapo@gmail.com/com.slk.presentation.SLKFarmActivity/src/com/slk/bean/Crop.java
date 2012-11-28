package com.slk.bean;

import java.io.Serializable;

public class Crop implements Serializable
{
	private static final long serialVersionUID = 7465804416814404007L;
	private String cultivarName;
	private String cropName;
	private String cropType;
	private int maxProduction;
	private int currentProduction;
	private double percentageProduction;
	private int quantity = 0;
	private boolean selected;
	
	
	
	

	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getCultivarName() {
		return cultivarName;
	}
	public void setCultivarName(String cultivarName) {
		this.cultivarName = cultivarName;
	}
	public String getCropName() {
		return cropName;
	}
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}
	public String getCropType() {
		return cropType;
	}
	public void setCropType(String cropType) {
		this.cropType = cropType;
	}
	public int getMaxProduction() {
		return maxProduction;
	}
	public void setMaxProduction(int maxProduction) {
		this.maxProduction = maxProduction;
	}
	public int getCurrentProduction() {
		return currentProduction;
	}
	public void setCurrentProduction(int currentProduction) {
		this.currentProduction = currentProduction;
	}
	public double getPercentageProduction() {
		return percentageProduction;
	}
	public void setPercentageProduction(double percentageProduction) {
		this.percentageProduction = percentageProduction;
	}
	
	
}	