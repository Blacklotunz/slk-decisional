package com.slk.bean;

public class Product{

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
	private String colorr,weight,size;

	public Product(String id, String name, String variety, double price, String color,String weight,String size,String imgURL, int productionLevel, int lista, Double max_production,Double current_production){
		this.id=id;
		this.name=name;
		this.variety=variety;
		this.price=price;
		this.setColorr(color);
		this.setWeight(weight);
		this.setSize(size);
		this.imgURL=imgURL;
		this.productionLevel=productionLevel;
		this.lista=lista;
		this.max_production=max_production;
		this.current_production=current_production;	
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


	public String getColorr() {
		return colorr;
	}

	public void setColorr(String colorr) {
		this.colorr = colorr;
	}


	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}