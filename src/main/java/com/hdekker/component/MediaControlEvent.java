package com.hdekker.component;

public class MediaControlEvent {
	
	final String ssc;
	final String date;
	
	public MediaControlEvent(String ssc, String date) {
		super();
		this.ssc = ssc;
		this.date = date;
	}
	
	public String getSsc() {
		return ssc;
	}
	public String getDate() {
		return date;
	} 
	
}
