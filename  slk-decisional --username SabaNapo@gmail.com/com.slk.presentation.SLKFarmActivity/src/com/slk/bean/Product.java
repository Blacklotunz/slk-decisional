package com.slk.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{

	private String id;
	private String nome;
	private String variety;
	private double prezzo;
	private String imgURL;
	private int colore;
	private int productionLevel;
	private int lista; //1 green, 2 yellow, 3 red
	private int q_vend_anno_precedente;
	private int q_prev_anno_corrente;

	public Product(String id, String nome, String variety, double prezzo, String imgURL, int productionLevel, int lista, int q_vend_anno_precedente,int q_prev_anno_corrente){
		this.id=id;
		this.nome=nome;
		this.prezzo=prezzo;
		this.imgURL=imgURL;
		this.productionLevel=productionLevel;
		this.lista=lista;
		this.q_vend_anno_precedente=q_vend_anno_precedente;
		this.q_prev_anno_corrente=q_prev_anno_corrente;
		this.setVariety(variety);
	}
	
	public Product(Parcel in) {
		this.nome=in.readString();
		this.prezzo=in.readDouble();
		this.imgURL=in.readString();
		this.colore=in.readInt();
		this.lista=in.readInt();
		this.q_vend_anno_precedente=in.readInt();
		this.q_prev_anno_corrente=in.readInt();	
	}
	
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(getNome());
		p.writeDouble(getPrezzo());
		p.writeString(getImg());
		p.writeInt(getColore());
		p.writeInt(getLista());
		p.writeInt(getQ_vend_anno_precedente());
		p.writeInt(getQ_prev_anno_corrente());
	}

	public int getQ_prev_anno_corrente() {
		return q_prev_anno_corrente;
	}

	public void setQ_prev_anno_corrente(int q_prev_anno_corrente) {
		this.q_prev_anno_corrente = q_prev_anno_corrente;
	}

	public int getQ_vend_anno_precedente() {
		return q_vend_anno_precedente;
	}

	public void setQ_vend_anno_precedente(int q_vend_anno_precedente) {
		this.q_vend_anno_precedente = q_vend_anno_precedente;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}

	public int getColore() {
		return colore;
	}

	public void setColore(int colore) {
		this.colore = colore;
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




	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

	public String getVariety() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}

}