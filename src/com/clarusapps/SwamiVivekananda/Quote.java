package com.clarusapps.SwamiVivekananda;

public class Quote {
	String quote;
	int id,fav;
	
	public Quote(){
		
	}
	public void setQuoteId(int id){
		this.id=id;
	}
	public void setQuote(String quote){
		this.quote=quote;
	}
	public void setFav(int fav){
		this.fav=fav;
	}
	public int getQuoteId(){
		return this.id;
	}
	public String getQuote(){
		return this.quote;
	}
	public int getFav(){
		return this.fav;
	}
	public Quote(int id,String quote,int fav){
		this.id=id;
		this.quote=quote;
		this.fav=fav;
	}
}

