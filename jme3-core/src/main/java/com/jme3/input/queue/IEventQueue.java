package com.jme3.input.queue;

import com.jme3.input.event.InputEvent;

public interface IEventQueue {
	
	/**
	 * Add event ot Queue.
	 * @param event
	 */
	public void add(InputEvent event);

	/**
	 * Process the evenQueue
	 */
	public void processQueue();
}
