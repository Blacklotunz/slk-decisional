package com.slk.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class HistoryProdotto implements Parcelable{

	private String nome, id, variety;
	private double prezzo;
	private String img;
	private int colore;
	private int anno;
	private int mese;
	private Double q_vend_anno_precedente;
	private Double q_prev_anno_corrente;
	private Double q_prodotta;

	public HistoryProdotto(String id, String nome,String variety, double prezzo, String img, int colore,int anno,int mese,Double q_vend_anno_precedente,Double q_prev_anno_corrente,Double q_prodotta){
		this.id=id;
		this.nome=nome;
		this.variety=variety;
		this.prezzo=prezzo;
		this.colore=colore;
		this.anno=anno;
		this.mese=mese;
		this.img=img;
		this.q_vend_anno_precedente=q_vend_anno_precedente;
		this.q_prev_anno_corrente=q_prev_anno_corrente;
		this.q_prodotta=q_prodotta;
	}
	
	public HistoryProdotto(Parcel in) {
		this.id=in.readString();
		this.nome=in.readString();
		this.variety=in.readString();
		this.prezzo=in.readDouble();
		this.img=in.readString();
		this.colore=in.readInt();
		this.anno=in.readInt();
		this.mese=in.readInt();
		this.q_vend_anno_precedente=in.readDouble();
		this.q_prev_anno_corrente=in.readDouble();
		this.q_prodotta=in.readDouble();
		
	}
	
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(getId());
		p.writeString(getNome());
		p.writeString(getVariety());
		p.writeDouble(getPrezzo());
		p.writeString(getImg());
		p.writeInt(getColore());
		p.writeInt(getAnno());
		p.writeInt(getMese());
		p.writeDouble(getQ_vend_anno_precedente());
		p.writeDouble(getQ_prev_anno_corrente());
		p.writeDouble(getQ_prodotta());
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

	public Double getQ_prodotta() {
		return q_prodotta;
	}

	public void setQ_prodotta(Double q_prodotta) {
		this.q_prodotta = q_prodotta;
	}


	public Double getQ_prev_anno_corrente() {
		return q_prev_anno_corrente;
	}

	public void setQ_prev_anno_corrente(Double q_prev_anno_corrente) {
		this.q_prev_anno_corrente = q_prev_anno_corrente;
	}

	public Double getQ_vend_anno_precedente() {
		return q_vend_anno_precedente;
	}

	public void setQ_vend_anno_precedente(Double q_vend_anno_precedente) {
		this.q_vend_anno_precedente = q_vend_anno_precedente;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
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
		return 0;
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

	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

}