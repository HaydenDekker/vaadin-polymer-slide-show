package com.hdekker.component;

import java.util.List;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface SSModel extends TemplateModel{

	public List<MediaDefinitionDTO> getMediaDefinitions();
	public void setMediaDefinitions(List<MediaDefinitionDTO> mediaDefinitions);
	public MediaControlEvent getMDC();
	public void setMDC(MediaControlEvent event);
	
}
