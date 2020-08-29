package com.hdekker.component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceRegistry;
import com.vaadin.flow.server.VaadinSession;

@Route("media-display")
@JsModule("./src/views/component/slide-show.js")
@Tag("media-ss-display")
public class MediaSSDisplay extends PolymerTemplate<SSModel> {

    @Id
    private TextField name;

    @Autowired
    TestMediaConfig config;
    
    @Id
    private Div mediaHolder;
    
    HorizontalLayout hz = new HorizontalLayout();
    Button next = new Button("next");
    
    public MediaSSDisplay( 
    		TestMediaConfig config) {
    	this.config = config;
    	
        setId("hello-world-view");
        mediaHolder.addClickListener( e-> {
            Notification.show("Hello " + name.getValue());
        });
        
        mediaHolder.add(hz);
        hz.add(next);
       
        next.addClickListener(nxt->  
        				getModel().setMDC(
        						new MediaControlEvent("forward", 
        											LocalDateTime.now().toString())
        						));
        
        StreamResourceRegistry reg = VaadinSession.getCurrent().getResourceRegistry(); 
        
        List<Media> regs = config.getMediaURLS()
							.stream()
							.map(m -> new Media(getMemoryBuffer()
									.andThen(memBufToStream())
									.andThen(registerResource(reg))
									.andThen(getURIOfStream())
									.apply(m), 
									m.getFileName(), 
									m.getContentType(),
									m.getDuration())
							).collect(Collectors.toList());
        
        getModel().setImageURLs(regs);
        
    }
    
    Function<StreamRegistration, String> getURIOfStream(){
    	return reg-> reg.getResourceUri().getPath();
    }
    
//    Function<List<StreamRegistration>, List<String>> urisForCLient(){
//    	return regs-> regs.stream().map(r->r.getResourceUri().getPath()).collect(Collectors.toList());
//    }
    
    Function<StreamResource, StreamRegistration> registerResource(StreamResourceRegistry reg){
    	return res -> reg.registerResource(res);
    	
    }
    
//    Function<List<StreamResource>, List<StreamRegistration>> registerResources(StreamResourceRegistry reg){
//    	return res -> res.stream().map(r->reg.registerResource(r)).collect(Collectors.toList());
//    	
//    }
    
    Function<Media, MemoryBuffer> getMemoryBuffer(){
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
    
//    Function<List<Media>, List<MemoryBuffer>> getMemoryBuffers(){
//    	return urls -> urls.stream().map((url)-> {
//    		
//    		File f = new File(url.getUrl());
//    		MemoryBuffer mb = new MemoryBuffer();
//    		try {
//				FileUtils.copyFile(f, mb.receiveUpload(url.getFileName(), ""));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		return mb;
//    	})
//    	.collect(Collectors.toList());
//    	
//    }
    
    Function<MemoryBuffer, StreamResource> memBufToStream(){
    	return buf -> new StreamResource(buf.getFileName(), ()-> buf.getInputStream());
    }
    
//    Function<List<MemoryBuffer>, List<StreamResource>> buffersToResources(){
//    	return buffs ->  buffs.stream()
//    							.map(b-> new StreamResource(b.getFileData().getFileName(), ()-> b.getInputStream()))
//    							.collect(Collectors.toList());
//    }
}
