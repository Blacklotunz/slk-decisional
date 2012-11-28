package com.slk.bean;

import java.io.Serializable;

public class Farm implements Serializable
{
	private static final long serialVersionUID = 7465804156714404007L;
	private int id;
	private String name;
	private int agroGeolocigalRegionId;
	private int farmerId;
	
	
	
	public int getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(int farmerId) {
		this.farmerId = farmerId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAgroGeolocigalRegionId() {
		return agroGeolocigalRegionId;
	}
	public void setAgroGeolocigalRegionId(int agroGeolocigalRegionId) {
		this.agroGeolocigalRegionId = agroGeolocigalRegionId;
	}

	
	
}
