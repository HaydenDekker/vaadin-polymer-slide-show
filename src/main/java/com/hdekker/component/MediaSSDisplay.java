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
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
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

@JsModule("./src/views/component/slide-show.js")
@Tag("media-ss-display")
public class MediaSSDisplay extends PolymerTemplate<SSModel> {
  
    @Id
    private Div mediaHolder;
    
    public MediaSSDisplay( ) {
    	
        setId("ss-media-display");
        
    }
    
    /**
     * Command needs to be transformed to a string to send to client,
     * Vaadin needs to detect change to trigger update hence timestamp
     * sent. TODO assumption maybe wrong.
     * 
     * @param command
     */
    public void sendCommand(SlideShowCommandType command) {
    	getModel().setMDC(
				new MediaControlEvent(command.getValue(), 
									LocalDateTime.now().toString())
		);
    }
    
    public void setMediaDefinitions(List<MediaDefinitionDTO> dtos) {
    	getModel().setMediaDefinitions(dtos);
    }
    
}
