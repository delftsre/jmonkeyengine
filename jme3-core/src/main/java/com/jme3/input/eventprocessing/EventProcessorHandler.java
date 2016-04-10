package com.jme3.input.eventprocessing;

import java.util.List;

import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.util.SafeArrayList;

public class EventProcessorHandler implements IEventProcessor{
	
    private final SafeArrayList<EventProcessor> eventProcessors = new SafeArrayList<EventProcessor>(EventProcessor.class);

    /* (non-Javadoc)
	 * @see com.jme3.input.IEventProcessor#add(com.jme3.input.EventProcessor)
	 */
    @Override
	public void add(EventProcessor evp)
    {
    	eventProcessors.add(evp);
    }
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IEventProcessor#remove(com.jme3.input.EventProcessor)
	 */
    @Override
	public void remove(EventProcessor evp)
    {
    	eventProcessors.remove(evp);
    }

	/* (non-Javadoc)
	 * @see com.jme3.input.IEvenProcessor#emit(java.util.List)
	 */
	@Override
	public void emit(List<InputEvent> inputQueue)
	{
		int queueSize = inputQueue.size();
		
        for (int i = 0; i < queueSize; i++) {
            InputEvent event = inputQueue.get(i);
            if (event.isConsumed()) {
                continue;
            }

            for(EventProcessor evp : eventProcessors)
            {
            	evp.processEvent(event);
            }
            
            // TODO: Check if this is true?
            // larynx, 2011.06.10 - flag event as reusable because
            // the android input uses a non-allocating ringbuffer which
            // needs to know when the event is not anymore in inputQueue
            // and therefor can be reused.
            event.setConsumed();
        }
	}
}
