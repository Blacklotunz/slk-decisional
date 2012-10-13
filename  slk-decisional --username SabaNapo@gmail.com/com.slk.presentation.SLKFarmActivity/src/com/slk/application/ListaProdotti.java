package com.slk.application;

import java.util.ArrayList;

public class ListaProdotti <E>{
	private ArrayList<E> prodotti;
	private int lista; // 0 per verde, 1 per giallo
	
	public ListaProdotti() {
		prodotti=new ArrayList<E>();
	}
	
	public ListaProdotti(ArrayList<E> prodotti, int lista) {
		setProdotti(prodotti);
		setLista(lista);
	}
	
	public void setLista(int lista) {
		this.lista = lista;
	}
	
	public void setProdotti(ArrayList<E>prodotti) {
		this.prodotti = prodotti;
	}
	
	public int getLista() {
		return lista;
	}
	
	public ArrayList<E> getProdotti() {
		return prodotti;
	}
	
	public void add(E prodotto) {
		prodotti.add(prodotto);
	}
	
	public void add(E prodotto, int index){
		prodotti.add(index, prodotto);
	}
	
	public boolean remove(Prodotto prodotto){
		return prodotti.remove(prodotto);
	}
	
	public E remove(int index){
		return  prodotti.remove(index);
	}
}
