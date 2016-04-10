package com.jme3.input.eventprocessing;

import java.util.List;

import com.jme3.input.event.InputEvent;

public interface IEventProcessor {

	/**
	 * Will emit the inputEvents to all registered EventProcessors
	 * @param inputQueue
	 */
	void emit(List<InputEvent> inputQueue);
	
	/**
	 * Add evenProcessor to the list over EVP which will receive
	 * events. 
	 * @param evp
	 */
	void add(EventProcessor evp);

	/**
	 * Removes eventProcessor from the list of Evp wich will receive events.
	 * @param evp
	 */
	void remove(EventProcessor evp);

}