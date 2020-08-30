package com.hdekker.component;

public class MediaDefinitionDTO {

	final String url;
	final String fileName;
	final String contentType;
	final Integer duration;
	
	public MediaDefinitionDTO(String url, String fileName, String contentType, Integer duration) {
		super();
		this.url = url;
		this.fileName = fileName;
		this.contentType = contentType;
		this.duration = duration;
	}
	public String getUrl() {
		return url;
	}
	public String getFileName() {
		return fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public Integer getDuration() {
		return duration;
	}
	
}
