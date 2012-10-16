package com.slk.application;

import com.slk.bean.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryProdotto implements Parcelable{

	private String nome;
	private double prezzo;
	private int img;
	private int colore;
	private int anno;
	private int mese;
	private int q_vend_anno_precedente;
	private int q_prev_anno_corrente;
	private int stagione;
	private int q_prodotta;

	public HistoryProdotto(String nome, double prezzo, int img, int colore,int anno,int mese,int q_vend_anno_precedente,int q_prev_anno_corrente,int stagione,int q_prodotta){
		this.nome=nome;
		this.prezzo=prezzo;
		this.img=img;
		this.colore=colore;
		this.anno=anno;
		this.mese=mese;
		this.q_vend_anno_precedente=q_vend_anno_precedente;
		this.q_prev_anno_corrente=q_prev_anno_corrente;
		this.stagione=stagione;
		this.q_prodotta=q_prodotta;
	}
	
	public HistoryProdotto(Parcel in) {
		this.nome=in.readString();
		this.prezzo=in.readDouble();
		this.img=in.readInt();
		this.colore=in.readInt();
		this.anno=in.readInt();
		this.mese=in.readInt();
		this.q_vend_anno_precedente=in.readInt();
		this.q_prev_anno_corrente=in.readInt();
		this.stagione=in.readInt();
		this.q_prodotta=in.readInt();
		
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public int getQ_prodotta() {
		return q_prodotta;
	}

	public void setQ_prodotta(int q_prodotta) {
		this.q_prodotta = q_prodotta;
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
		p.writeInt(getAnno());
		p.writeInt(getMese());
		p.writeInt(getQ_vend_anno_precedente());
		p.writeInt(getQ_prev_anno_corrente());
		p.writeInt(getStagione());
		p.writeInt(getQ_prodotta());
	}

	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

}