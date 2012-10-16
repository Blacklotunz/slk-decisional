package com.slk.application;

import android.os.Parcel;
import android.os.Parcelable;

public class Crop implements Parcelable{

	private String nome;
	private double prezzo;
	private int img;
	private int colore;
	private int lista;
	private int q_vend_anno_precedente;
	private int q_prev_anno_corrente;
	private int stagione;

	public Crop(String nome, double prezzo, int img, int colore,int lista,int q_vend_anno_precedente,int q_prev_anno_corrente,int stagione){
		this.nome=nome;
		this.prezzo=prezzo;
		this.img=img;
		this.colore=colore;
		this.lista=lista;
		this.q_vend_anno_precedente=q_vend_anno_precedente;
		this.q_prev_anno_corrente=q_prev_anno_corrente;
		this.stagione=stagione;
	}
	
	public Crop(Parcel in) {
		this.nome=in.readString();
		this.prezzo=in.readDouble();
		this.img=in.readInt();
		this.colore=in.readInt();
		this.lista=in.readInt();
		this.q_vend_anno_precedente=in.readInt();
		this.q_prev_anno_corrente=in.readInt();
		this.stagione=in.readInt();
		
	}

	public int getStagione() {
		return stagione;
	}

	public void setStagione(int stagione) {
		this.stagione = stagione;
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

	public int getImg() {
		return img;
	}


	public void setImg(int img) {
		this.img = img;
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
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(getNome());
		p.writeDouble(getPrezzo());
		p.writeInt(getImg());
		p.writeInt(getColore());
		p.writeInt(getLista());
		p.writeInt(getQ_vend_anno_precedente());
		p.writeInt(getQ_prev_anno_corrente());
		p.writeInt(getStagione());
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Crop createFromParcel(Parcel in) {
			return new Crop(in);
		}
		public Crop[] newArray(int size) {
			return new Crop[size];
		}
	};

}