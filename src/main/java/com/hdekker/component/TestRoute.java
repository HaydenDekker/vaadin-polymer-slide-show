package com.hdekker.component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResourceRegistry;
import com.vaadin.flow.server.VaadinSession;

@Route("test-route")
public class TestRoute extends VerticalLayout implements AfterNavigationObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	TestMediaConfig config;
	
	MediaSSDisplay display;
	
	HorizontalLayout controls = new HorizontalLayout();
    Button next = new Button("next");
    Button back = new Button("back");
    Button play = new Button("play");
    Button stop = new Button("stop");
    Button pause = new Button("pause");
	 
	public TestRoute() {
		
		setHeightFull();
		add(controls);
		controls.add(next, back, play, stop, pause);
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		display = new MediaSSDisplay();
		add(display);
		
		// TODO this causes memory leaks, Ok for demonstration, but bear in mind.
		StreamResourceRegistry reg = VaadinSession.getCurrent().getResourceRegistry(); 
		
		Function<MediaDefinitionDTO, String> mediaObjectStreamURI = MediaManagementUtils.getMemoryBuffer()
				.andThen(MediaManagementUtils.memBufToStream())
				.andThen(MediaManagementUtils.registerResource(reg))
				.andThen(MediaManagementUtils.getURIOfStream());
		
		List<MediaDefinitionDTO> mediaDTOS = config.getMediaURLS()
				.stream()
				.map(m -> new MediaDefinitionDTO(
						mediaObjectStreamURI.apply(m), 
						m.getFileName(), 
						m.getContentType(),
						m.getDuration())
				).collect(Collectors.toList());
		
        display.setMediaDefinitions(mediaDTOS);
        
		next.addClickListener(nxtE->  
			display.sendCommand(SlideShowCommandType.FORWARD)
		);
		
		back.addClickListener(backE -> display.sendCommand(SlideShowCommandType.BACK));
		play.addClickListener(playE -> display.sendCommand(SlideShowCommandType.PLAY));
		stop.addClickListener(stopE -> display.sendCommand(SlideShowCommandType.STOP));
		pause.addClickListener(pauseE -> display.sendCommand(SlideShowCommandType.PAUSE));
		
		
	}
	
	
}
