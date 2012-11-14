package com.slk.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{

	/**
	 * Product bean
	 * 
	 */
	private String id;
	private String name;
	private String variety;
	private double price;
	private String imgURL;
	private int color;
	private int productionLevel;
	private int lista; //1 green, 2 yellow, 3 red
	private Double max_production;
	private Double current_production;

	public Product(String id, String name, String variety, double price, String imgURL, int productionLevel, int lista, Double max_production,Double current_production){
		this.id=id;
		this.name=name;
		this.variety=variety;
		this.price=price;
		this.imgURL=imgURL;
		this.productionLevel=productionLevel;
		this.lista=lista;
		this.max_production=max_production;
		this.current_production=current_production;
		
	}
	
	public Product(Parcel in) {
		this.id = in.readString();
		this.name=in.readString();
		this.variety=in.readString();
		this.price=in.readDouble();
		this.imgURL=in.readString();
		this.color=in.readInt();
		this.productionLevel=in.readInt();
		this.lista=in.readInt();
		this.max_production=in.readDouble();
		this.current_production=in.readDouble();	
	}
	
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(getId());
		p.writeString(getName());
		p.writeString(getVariety());
		p.writeDouble(getPrice());
		p.writeString(getImg());
		p.writeInt(getColor());
		p.writeInt(getProductionLevel());
		p.writeInt(getLista());
		p.writeDouble(getMax_production());
		p.writeDouble(getCurrent_production());
	}

	public Double getCurrent_production() {
		return current_production;
	}

	public void setcurrent_production(Double current_production) {
		this.current_production = current_production;
	}

	public Double getMax_production() {
		return max_production;
	}

	public void setMax_production(Double max_production) {
		this.max_production = max_production;
	}

	

	public int getLista() {
		return lista;
	}

	public void setLista(int lista) {
		this.lista = lista;
	}

	public String getImg() {
		return imgURL;
	}


	public void setImg(String img) {
		this.imgURL = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}


	
	public int describeContents() {
		return 0;
	}

	public int getProductionLevel() {
		return productionLevel;
	}

	public void setProductionLevel(int productionLevel) {
		this.productionLevel = productionLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getVariety() {
		return variety;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}


	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

}