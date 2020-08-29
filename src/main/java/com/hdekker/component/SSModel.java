package com.hdekker.component;

import java.util.List;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface SSModel extends TemplateModel{

	public List<Media> getImageURLs();
	public void setImageURLs(List<Media> imageURLs);
	public MediaControlEvent getMDC();
	public void setMDC(MediaControlEvent event);
	
}
