package com.jme3.input.queue;

import java.util.ArrayList;

import com.jme3.input.IBaseListenerEmitter;
import com.jme3.input.event.InputEvent;
import com.jme3.input.eventprocessing.IEventProcessor;

public class EventQueue implements IEventQueue{
	
    private final ArrayList<InputEvent> inputQueue = new ArrayList<InputEvent>();
    private IBaseListenerEmitter baselisteners;
    private IEventProcessor eventprocessors;
	public boolean eventsPermitted = false;
	
    /**
     * Initializes the EventQueue
     */
    public EventQueue( IBaseListenerEmitter listener, IEventProcessor eventProcessor ) {
        baselisteners = listener;
        eventprocessors = eventProcessor;
    }
    
	/* (non-Javadoc)
	 * @see com.jme3.input.IQueueProcessor#processQueue()
	 */
    @Override
	public void processQueue() {        
        baselisteners.emit(inputQueue);

        eventprocessors.emit(inputQueue);

        inputQueue.clear();
    }


	@Override
	public void add(InputEvent event) {
		 inputQueue.add(event);
	}
	
    public IBaseListenerEmitter getBaselisteners() {
		return baselisteners;
	}

	public IEventProcessor getEventprocessors() {
		return eventprocessors;
	}
}
