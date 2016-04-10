package com.jme3.input.eventprocessing;

import com.jme3.input.event.InputEvent;

public interface EventProcessor {
	
	/**
	 * Process the event, if the event is not accepted the event
	 * will not be processed.
	 * @param event
	 */
	public void processEvent(InputEvent event);
	
	/**
	 * Returns true if the InputEvent is accepted by this EVP.
	 * @param event The event to be checked
	 * @return True if event will be processed
	 */
	public boolean accepts(InputEvent event);

}
