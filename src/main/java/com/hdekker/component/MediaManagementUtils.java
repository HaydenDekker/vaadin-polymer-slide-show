package com.hdekker.component;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceRegistry;

public interface MediaManagementUtils {

	  	static Function<StreamRegistration, String> getURIOfStream(){
	    	return reg-> reg.getResourceUri().getPath();
	    }
	    
	    static Function<StreamResource, StreamRegistration> registerResource(StreamResourceRegistry reg){
	    	return res -> {
	    		return reg.registerResource(res);
	    	};
	    	
	    }

	    static Function<MediaDefinitionDTO, MemoryBuffer> getMemoryBuffer(){
	    	return media -> {
	    		File f = new File(media.getUrl());
	    		MemoryBuffer mb = new MemoryBuffer();
	    		try {
					FileUtils.copyFile(f, mb.receiveUpload(media.getFileName(), media.getContentType()));
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		return mb;
	    	};
	    	
	    }
	    
	    static Function<MemoryBuffer, StreamResource> memBufToStream(){
	    	return buf -> new StreamResource(buf.getFileName(), ()-> buf.getInputStream());
	    }
	
}
