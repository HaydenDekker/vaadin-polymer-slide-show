package com.hdekker.component;

/**
 * General playlist style events.
 * 
 * 
 * @author HDekker
 *
 */
public enum SlideShowCommandType {
	
	PLAY("play"),
	STOP("stop"),
	PAUSE("pause"),
	BACK("back"),
	FORWARD("forward");
	
	final String value; 
	
	SlideShowCommandType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
};
