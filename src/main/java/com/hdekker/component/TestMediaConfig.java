package com.hdekker.component;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TestMediaConfig {

	
	final List<Media> mediaURLS;
	
	public TestMediaConfig(){
		mediaURLS = Arrays.asList(new Media("C:/Users/HDekker/Documents/Camera/20180106_181002.jpg","20180106_181002.jpg", "image/jpeg", 10),
		new Media("C:/Users/HDekker/Documents/Camera/20180106_181043.jpg", "20180106_181043.jpg", "image/jpeg", 5),
		new Media("C:/Users/HDekker/Documents/Camera/20180124_140649.mp4", "20180124_140649.mp4", "video/mp4", 6));
	}

	public List<Media> getMediaURLS() {
		return mediaURLS;
	}
	
	
}
