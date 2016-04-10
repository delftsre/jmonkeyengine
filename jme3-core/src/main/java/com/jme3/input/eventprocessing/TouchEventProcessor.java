package com.jme3.input.eventprocessing;

import com.jme3.input.ActionInvoker;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.TouchEvent;

public class TouchEventProcessor implements EventProcessor {

	private ActionInvoker invoker;
	
	public TouchEventProcessor(ActionInvoker actionInvoker)
	{
		invoker = actionInvoker;
	}
	
	@Override
	public void processEvent(InputEvent event) {
		if(event instanceof TouchEvent)
		{
			invoker.invokeTouchActions((TouchEvent) event);
		}

	}

	@Override
	public boolean accepts(InputEvent event) {
		return event instanceof TouchEvent;
	}

}
