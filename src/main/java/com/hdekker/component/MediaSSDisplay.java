package com.hdekker.component;

import java.time.LocalDateTime;
import java.util.List;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@JsModule("./src/views/component/slide-show.js")
@Tag("media-ss-display")
public class MediaSSDisplay extends PolymerTemplate<SSModel> {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
